package com.example.journey_server.Service;

import com.example.journey_server.entity.Peer;
import com.example.journey_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MatchService {


    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    @Autowired
    private RedisUtil redisUtil;


    public double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    public double[] geo2xyz(double lon, double lat) {
        double thera = (Math.PI * lat) / 180;
        double fie = (Math.PI * lon) / 180;
        double x = EARTH_RADIUS * Math.cos(thera) * Math.cos(fie);
        double y = EARTH_RADIUS * Math.cos(thera) * Math.sin(fie);
        double z = EARTH_RADIUS * Math.sin(thera);
        double[] point = new double[3];
        point[0] = x;
        point[1] = y;
        point[2] = z;
        return point;
    }

    /**
     * @param lon1
     * @param lat1
     * @param lon2 start point
     * @param lat2 start point
     * @param lon3
     * @param lat3
     * @return
     */
    public double getAngel(double lon1, double lat1, double lon2, double lat2, double lon3, double lat3) {
        double[] p1 = geo2xyz(lon1, lat1);
        double[] p2 = geo2xyz(lon2, lat2);
        double[] p3 = geo2xyz(lon3, lat3);
        double P1P2 = Math.sqrt((p2[0] - p1[0]) * (p2[0] - p1[0]) + (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[2] - p1[2]) * (p2[2] - p1[2]));
        double P2P3 = Math.sqrt((p3[0] - p2[0]) * (p3[0] - p2[0]) + (p3[1] - p2[1]) * (p3[1] - p2[1]) + (p3[2] - p2[2]) * (p3[2] - p2[2]));
        double P = (p1[0] - p2[0]) * (p3[0] - p2[0]) + (p1[1] - p2[1]) * (p3[1] - p2[1]) + (p1[2] - p2[2]) * (p3[2] - p2[2]);
        return (Math.acos(P / (P1P2 * P2P3)) / Math.PI) * 180;
    }

    public List<Peer> getMatch(Peer user) {
        if (user.getLimit() == null || user.getLimit() == 0) {
            user.setLimit(5);
        }
        Map<String, List<Peer>> matchedUser = redisUtil.getMatchedUser();
        List<Peer> peers = matchedUser.get(user.getEmail());
        if(peers == null || peers.size() == 0){
            peers = new ArrayList<>();
            user.setLeader(true);
            peers.add(user);
        }

        String uuid = UUID.randomUUID().toString();

        for (Map.Entry<String, Peer> entry : redisUtil.getUsers().entrySet()) {
            Peer userM = entry.getValue();
            if (user.getEmail().equals(userM.getEmail())) {
                continue;
            }
            System.out.println("matching--maxAge:"+user.getMaxAge()+"--minAge:"+user.getMinAge()+"--memberage:"+userM.getAge());
            if (userM.getAge() > user.getMaxAge() || userM.getAge() < user.getMinAge()) {
                continue;
            }
            System.out.println("matching--gender:"+user.getGenderCon()+"--membergender:"+userM.getGender());
            if(!user.getGenderCon().equals("Other") && !user.getGenderCon().equals(userM.getGender())){
                continue;
            }
            System.out.println("matching--distance:"+getDistance(user.getLongitude(), user.getLatitude(), userM.getLongitude(), userM.getLatitude()));
            if (getDistance(user.getLongitude(), user.getLatitude(), userM.getLongitude(), userM.getLatitude()) >= 500) {
                continue;
            }
            System.out.println("matching--angle:"+getAngel(user.getdLongtitude(), user.getdLatitude(), user.getLongitude(), user.getLongitude(),
                    userM.getdLongtitude(), userM.getdLatitude()));
            if (getAngel(user.getdLongtitude(), user.getdLatitude(), user.getLongitude(), user.getLongitude(),
                    userM.getdLongtitude(), userM.getdLatitude()) >= 45) {
                continue;
            }
            peers.add(userM);
            calFurthest(peers);
            addUuid(peers, uuid);
            redisUtil.removeUser(userM.getEmail());
            redisUtil.putMatchedUser(user.getEmail(), peers);
            if (peers.size() >= user.getLimit()) {
                return peers;
            }
        }
        calFurthest(peers);
        addUuid(peers, uuid);
        redisUtil.putMatchedUser(user.getEmail(), peers);
        for (Peer peer : peers) {
            System.out.println(peer.getEmail() + ":" + peer.getLeader());
        }

        return peers;
    }

    private void addUuid(List<Peer> result, String uuid) {
        if (result.size() == 0) {
            return;
        }
        for (Peer peer : result) {
            peer.setUuid(uuid);
        }

    }

    public void calFurthest(List<Peer> peers) {
        if (peers.size() == 0) {
            return;
        }
        String email = null;
        double maxd = Long.MIN_VALUE;
        for (Peer peer : peers) {
            double d1 = getDistance(peer.getdLongtitude(), peer.getLatitude(), peer.getdLongtitude(), peer.getdLatitude());
            if (d1 > maxd) {
                maxd = d1;
                email = peer.getEmail();
            }
        }

        for (Peer peer : peers) {
            if (peer.getEmail().equals(email)) {
                peer.setFurthest(true);
            }
        }
    }

    public List<Peer> getMatchMember(Peer peer) {
        redisUtil.addUser(peer);
        for (Map.Entry<String, List<Peer>> entry : redisUtil.getMatchedUser().entrySet()) {
            for (Peer userM : entry.getValue()) {
                if (peer.getEmail().equals(userM.getEmail())) {
                    return entry.getValue();
                }
            }
        }

        return new ArrayList<>();

    }


    public String createGroup(List<Peer> peers) {
        return "";
    }

    public Peer getLeader(List<Peer> peers) {
        for (Peer peer : peers) {
            if (peer.getLeader()) {
                return peer;
            }
        }
        return peers.get(0);
    }

}
