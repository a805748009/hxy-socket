package com.result.base.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserClient {
    //用户userId,client
    public static final Map<String,Client> userClient = new ConcurrentHashMap<>();

    public static Client getClient(String userId){
        return userClient.get(userId);
    }

    public static Client setClient(String userId,Client client){
        return userClient.put(userId,client);
    }

    public static void removeClient(String userId){
        userClient.remove(userId);
    }
}
