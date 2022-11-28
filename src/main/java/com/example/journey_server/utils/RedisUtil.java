package com.example.journey_server.utils;

import com.example.journey_server.entity.Peer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Component
public class RedisUtil {

    private static String ip = null;

    private static String port = null;

    private static Jedis jedis = null;

    private static String lock_key = "redis_lock";

    private static long internalLockLeaseTime = 3000;

    private static long timeout = 15000;

    private static final String LOCK_SUCCESS = "OK";

    private static final String SET_IF_NOT_EXIST = "NX";

    private static final String SET_WITH_EXPIRE_TIME = "PX";


    @Autowired
    private SerializeUtil serializeUtil;

    static {
        Properties pro = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("src/main/resources/redis.properties");
            pro.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ip = pro.getProperty("ip");
        port = pro.getProperty("port");
        jedis = new Jedis(ip, Integer.parseInt(port));

    }

    public Jedis getJedis() {
        return jedis;
    }

    public void addUser(Peer user) {
        boolean result = checkAllUserPool(user);
        if(!result){
            byte[] bytes = jedis.get("users".getBytes());
            Map<String, Peer> users = null;
            if (null == bytes) {
                users = new HashMap<>();
            } else {
                Map<String, Peer> userss = (Map<String, Peer>) serializeUtil.unserizlize(bytes);
                users = userss;
            }
            users.put(user.getEmail(), user);
            jedis.set("users".getBytes(), serializeUtil.serialize(users));
        }

    }

    private boolean checkAllUserPool(Peer user) {
        byte[] bytes = jedis.get("allUsers".getBytes());
        boolean result = true;
        Map<String, Peer> allUsers = null;
        if (null == bytes) {
            allUsers = new HashMap<>();
            result = false;
        } else {
            Map<String, Peer> userss = (Map<String, Peer>) serializeUtil.unserizlize(bytes);
            allUsers = userss;
            Peer peer = userss.get(user.getEmail());
            if(peer == null){
                result = false;
            }
        }
        allUsers.put(user.getEmail(), user);
        jedis.set("allUsers".getBytes(), serializeUtil.serialize(allUsers));
        return result;
    }

    public Map<String, Peer> getUsers() {
        Map<String, Peer> users = new HashMap<>();
        byte[] bytes = jedis.get("users".getBytes());
        if (null == bytes) {
            return users;
        } else {
            users = (Map<String, Peer>) serializeUtil.unserizlize(bytes);
            return users;
        }
    }

    public void removeUser(String email) {
        byte[] bytes = jedis.get("users".getBytes());
        if (null == bytes) {
            return;
        }
        Map<String, Peer> users = (Map<String, Peer>) serializeUtil.unserizlize(bytes);
        if (users.containsKey(email)) {
            users.remove(email);
            jedis.set("users".getBytes(), serializeUtil.serialize(users));
        }
    }

    public void putMatchedUser(String email, List<Peer> peers) {
        byte[] bytes = jedis.get("matchedUser".getBytes());
        Map<String, List<Peer>> matched;
        if (null == bytes) {
            matched = new HashMap<>();
        } else {
            matched = (Map<String, List<Peer>>) serializeUtil.unserizlize(bytes);
        }
        matched.put(email, peers);
        jedis.set("matchedUser".getBytes(), serializeUtil.serialize(matched));
    }

    public Map<String, List<Peer>> getMatchedUser() {
        byte[] bytes = jedis.get("matchedUser".getBytes());
        if (null == bytes) {
            return new HashMap<>();
        } else {
            return (Map<String, List<Peer>>) serializeUtil.unserizlize(bytes);
        }
    }

    public boolean lock(String id) {
        Long start = System.currentTimeMillis();
        try {
            for (; ; ) {
                String lock = jedis.set(lock_key, id, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, internalLockLeaseTime);
                if (LOCK_SUCCESS.equals(lock)) {
                    return true;
                }
                long l = System.currentTimeMillis() - start;
                if (l >= timeout) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * @param id
     * @return
     */
    public boolean unlock(String id) {
        String script =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";
        try {
            Object result = jedis.eval(script, Collections.singletonList(lock_key),
                    Collections.singletonList(id));
            if ("1".equals(result.toString())) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }
}
