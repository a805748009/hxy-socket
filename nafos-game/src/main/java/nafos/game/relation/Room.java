package nafos.game.relation;


import nafos.game.entry.BaseUser;

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

    protected final ConcurrentHashMap<Object, ConcurrentHashMap<String, Object>> userData = new ConcurrentHashMap<>();//房间中用户信息

    protected final ConcurrentHashMap<String, Object> roomData = new ConcurrentHashMap<>();//房间配置

    protected final CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<>();//在线用户列表

    protected final CopyOnWriteArrayList<BaseUser> users = new CopyOnWriteArrayList<>();//当前用户列表

    public Room(String id) {
        this.id = id;
        IoCache.roomMap.put(id, this);
    }


    public void RoomInit() {
        roomData.clear();
        clearUnActiveUser();
    }


    /**
     * @param
     * @return void
     * @Author 黄新宇
     * @date 2018/8/4 下午5:28
     * @Description(清楚掉线的人)
     */
    public void clearUnActiveUser() {
        List<Object> l = new ArrayList<>();
        for (Client client : clients) {
            Object userId = client.getUserId();
            if (!client.getChannel().isActive()) {
                removeUser(client);
                userData.remove(userId);
                continue;
            }

            if (userData.containsKey(userId)) {
                l.add(userId);
            }
        }
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

    public void sendMsg(Object obj, byte[] id) {
        for (Client client : clients) {
            client.sendMsg(obj, id);
        }
    }


    //用户掉线，不删除在房间中的信息
    public void offLineClient(Client client) {
        clients.remove(client);
        if (clients.isEmpty()) {
            IoCache.roomMap.remove(id);
            NameSpace.removeRoom(client.getNameSpace(), id);
        }
    }


    //用户掉线，不删除在房间中的信息,即使掉光了，也不删除房间
    public void offLineClientNotDel(Client client) {
        clients.remove(client);
    }


    public void deleteRoomInCacheAndNameSpace() {
        IoCache.roomMap.remove(id);
        NameSpace.removeRoom(nameSpace, id);
    }

    //踢掉用户
    public void removeUser(String userId) {
        Client delClient = null;
        for (Client client : clients) {
            if (userId.equals(client.getUserId()))
                delClient = client;
        }
        removeUserNotDelRoom(delClient);

        synchronized (clients) {
            if (clients.isEmpty()) {
                deleteRoomInCacheAndNameSpace();
            }
        }
    }


    public void removeUser(Client client) {
        removeUserNotDelRoom(client);
        synchronized (clients) {
            if (clients.isEmpty()) {
                deleteRoomInCacheAndNameSpace();
            }
        }
    }

    //客户端离开房间但是不删除房间信息，让机器人删除
    public void removeUserNotDelRoom(Client client) {
        Object userId = client.getUserId();
        clients.remove(client);
        userData.remove(userId);
        for (BaseUser s : users) {
            if (s.getUserId().equals(userId)) {
                users.remove(s);
            }
        }
    }

    public void addClient(Client client) {
        synchronized (clients) {
            if (!clients.contains(client)) {
                clients.add(client);
            }
        }
    }

    public boolean containsUsers(Object userId) {
        boolean isContain = false;
        for (BaseUser s : users) {
            if (s.getUserId().equals(userId)) {
                isContain = true;
            }
        }
        return isContain;
    }

    public boolean containsUserDatas(String userId) {
        return userData.containsKey(userId);
    }

    public void addUser(BaseUser user) {
        synchronized (users) {
            if (!containsUsers(user.getUserId())) {
                users.add(user);
            }
        }
    }

    public void addClientAndUserData(Client client, Map<String, Object> map) {
        if (!clients.contains(client))
            clients.add(client);
        Object userId = client.getUserId();
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

    public Object getUserDataOnKey(Object userId, String key) {
        return userData.get(userId).get(key);
    }

    public Object getUserDataOnKey(Client client, String key) {
        Object userId = client.getUserId();
        return getUserDataOnKey(userId, key);
    }

    public Map<String, Object> getUserData(Object userId) {
        return userData.get(userId);
    }

    public Map<String, Object> getUserData(Client client) {
        Object userId = client.getUserId();
        return getUserData(userId);
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

    public void setUserDataOnkey(Client client, String key, Object object) {
        Object userId = client.getUserId();
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

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConcurrentHashMap<Object, ConcurrentHashMap<String, Object>> getUserData() {
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

}
