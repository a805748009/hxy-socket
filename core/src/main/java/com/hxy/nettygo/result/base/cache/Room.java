package com.hxy.nettygo.result.base.cache;

import com.hxy.nettygo.result.base.entry.Base.BaseUser;
import com.hxy.nettygo.result.base.inits.InitMothods;
import com.hxy.nettygo.result.base.security.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author 黄新宇
 * @Date 2018/5/5 下午5:15
 * @Description 房间
 **/
public class Room {

    protected String id;

    protected String nameSpace;

    protected int nowTime;

    protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> userData = new ConcurrentHashMap<>();//房间中用户信息

    protected final ConcurrentHashMap<String, Object> roomData = new ConcurrentHashMap<>();//房间配置

    protected final CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<>();//在线用户列表

    protected final CopyOnWriteArrayList<BaseUser> users = new CopyOnWriteArrayList<>();//当前机器人列表

    public Room(String id) {
        this.id = id;
        IoCache.roomMap.put(id, this);
    }


    public void RoomInit() {
        roomData.clear();
        List<String> l = new ArrayList<>();
        for (Client client : clients) {
            if (!client.getChannel().isActive()) {
                clients.remove(client);
                continue;
            }
            String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
            if (userData.containsKey(userId)) {
                l.add(userId);
            }
        }
        //清除掉线的人
        userData.keySet().forEach(userId -> {
            if (!l.contains(userId)) {
                userData.remove(userId);
            }
        });
    }

    public void RoomInitButSaveroomDATA() {
        List<String> l = new ArrayList<>();
        for (Client client : clients) {
            if (!client.getChannel().isActive()) {
                clients.remove(client);
                continue;
            }
            String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
            if (userData.containsKey(userId)) {
                l.add(userId);
            }
        }
        //清除掉线的人
        userData.keySet().forEach(userId -> {
            if (!l.contains(userId)) {
                userData.remove(userId);
            }
        });
    }

    public String getRoomId() {
        return id;
    }

    public Object getRoomDataOnKey(String key) {
        return roomData.get(key);
    }

    public void setRoomData(String key, Object object) {
        roomData.put(key, object);
    }

    public void clearRoomData() {
        roomData.clear();
    }

    public int getRoomOnLineNum() {
        return clients.size();
    }

    public void sendMsg(Object obj) {
        for (Client client : clients) {
            client.sendMsg(obj);
        }
    }

    public void sendMsg(Object obj,byte[] id) {
        for (Client client : clients) {
            client.sendMsg(obj,id);
        }
    }

    public void sendMsg(Object obj,String id) {
        for (Client client : clients) {
            client.sendMsg(obj,id);
        }
    }

    public void startCountTime() {
        this.nowTime++;
    }


    //用户掉线，不删除在房间中的信息
    public void offLineClient(Client client) {
        String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
        clients.remove(client);
        if (clients.isEmpty()) {
            IoCache.roomMap.remove(id);
            NameSpace.removeRoom(client.getNameSpace(), id);
        }
    }

    //客户端离开房间但是不删除房间信息，让机器人删除
    public void clientLeaveNotDelRoom(Client client) {
        String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
        clients.remove(client);
        for (BaseUser s : users) {
            if (s.getBaseUserId().equals(userId)) {
                users.remove(s);
            }
        }
    }

    public void deleteRoomInCacheAndNameSpace() {
        if (IoCache.roomMap.containsKey(id))
            IoCache.roomMap.remove(id);
        NameSpace.removeRoom(nameSpace, id);
    }

    //踢掉用户
    public void removeUser(String userId) {
        userData.remove(userId);
        for (Client client : clients) {
            if (userId.equals(((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId()))
                clients.remove(client);
        }
        for (BaseUser s : users) {
            if (s.getBaseUserId().equals(userId)) {
                users.remove(s);
            }
        }
        if (userData.isEmpty()) {
            IoCache.roomMap.remove(id);
            NameSpace.removeRoom(this.nameSpace, id);
        }
    }

    public void removeUser(Client client) {
        String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
        userData.remove(userId);
        clients.remove(client);
        for (BaseUser s : users) {
            if (s.getBaseUserId().equals(userId)) {
                users.remove(s);
            }
        }
        if (clients.isEmpty()) {
            IoCache.roomMap.remove(id);
            NameSpace.removeRoom(client.getNameSpace(), id);
        }
    }

    public void addUser(Client client) {
        if (!clients.contains(client)) {
            clients.add(client);
        }
    }

    public boolean userContainsInUsers(String user) {
        boolean isContain = false;
        for (BaseUser s : users) {
            if (s.getBaseUserId().equals(user)) {
                isContain = true;
            }
        }
        return isContain;
    }

    public boolean userContainsInUserDatas(String user) {
        return userData.contains(user);
    }

    public void addUserWithoutClient(BaseUser user) {
        if (!userContainsInUsers(user.getUserId())) {
            users.add(user);
        }
    }

    public void addUser(Client client, Map<String, Object> map) {
        if (!clients.contains(client))
            clients.add(client);
        String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
        for (String key : map.keySet()) {
            if (userData.containsKey(userId)) {
                userData.get(userId).put(key, map.get(key));
                continue;
            }
            ConcurrentHashMap<String, Object> userKeyData = new ConcurrentHashMap<>();
            userKeyData.put(key, map.get(key));
            userData.put(userId, userKeyData);
        }
    }

    public Object getUserDataOnKey(String userId, String key) {
        return userData.get(userId).get(key);
    }

    public Object getUserDataOnKey(Client client, String key) {
        String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
        return userData.get(userId).get(key);
    }

    public Map<String, Object> getUserData(String userId) {
        return userData.get(userId);
    }

    public Map<String, Object> getUserData(Client client) {
        String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
        return userData.get(userId);
    }

    public void setUserDataOnkey(String userId, String key, Object object) {
        if (userData.containsKey(userId)) {
            userData.get(userId).put(key, object);
            return;
        }
        ConcurrentHashMap<String, Object> userKeyData = new ConcurrentHashMap<>();
        userKeyData.put(key, object);
        userData.put(userId, userKeyData);
    }

    public int getClientCount() {
        return clients.size();
    }

    public void setUserDataOnkey(Client client, String key, Object object) {
        String userId = ((BaseUser) SecurityUtil.getLoginUser(client.getToken(), InitMothods.getUserClazz())).getBaseUserId();
        if (userData.containsKey(userId)) {
            userData.get(userId).put(key, object);
            return;
        }
        ConcurrentHashMap<String, Object> userKeyData = new ConcurrentHashMap<>();
        userKeyData.put(key, object);
        userData.put(userId, userKeyData);
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> getUserData() {
        return userData;
    }

    public ConcurrentHashMap<String, Object> getRoomData() {
        return roomData;
    }

    public CopyOnWriteArrayList<Client> getClients() {
        return clients;
    }

    public CopyOnWriteArrayList<BaseUser> getUsers() {
        return users;
    }

    public String getId() {
        return id;
    }

    public int getNowTime() {
        return nowTime;
    }

    public void setNowTime(int nowTime) {
        this.nowTime = nowTime;
    }
}
