package com.example.journey_server.Controller;

import com.example.journey_server.Service.MatchService;
import com.example.journey_server.entity.Peer;
import com.example.journey_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatchController {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final String LOCK_KEY = "key";

    private static long expireTime = 3000;
    @Autowired
    private MatchService matchService;

    @Autowired
    private RedisUtil redisUtil;


    @PostMapping("/matchLeader")
    public List<Peer> matchLeader(@RequestBody Peer peer) {
        System.out.println("matchLeader:" + peer.getEmail());
        List<Peer> usersList = matchService.getMatch(peer);
        System.out.println(usersList.size());
        return usersList;
    }


    @PostMapping("/matchMember")
    public List<Peer> matchMember(@RequestBody Peer peer) {
        System.out.println("matchMember:" + peer.getEmail());
        List<Peer> usersList = matchService.getMatchMember(peer);
//        System.out.println(usersList.size());
        return usersList;
    }


}
