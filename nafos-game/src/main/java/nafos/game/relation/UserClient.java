package nafos.game.relation;

import java.util.concurrent.ConcurrentHashMap;

public class UserClient {
    //用户userId,client
    public static final ConcurrentHashMap<Object, Client> userClient = new ConcurrentHashMap<>(1024);

    public static Client getClient(String userId) {
        return userClient.get(userId);
    }

    public static Client setClient(Object userId, Client client) {
        return userClient.put(userId, client);
    }

    public static void removeClient(String userId) {
        userClient.remove(userId);
    }
}
