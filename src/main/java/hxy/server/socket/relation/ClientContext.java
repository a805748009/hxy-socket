package hxy.server.socket.relation;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public class ClientContext extends Context {
    private final ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<>(1024);

    public ClientContext(String id) {
        super(id);
    }

    public void addClient(@NotNull Client client) {
        clients.put(client.getId(), client);
    }

    public void removeClient(@NotNull Client client) {
        clients.remove(client.getId());
    }

    public int clientCount() {
        return clients.size();
    }

    public Client getClient(String id) {
        return clients.get(id);
    }

    public boolean containsClient(String id) {
        return clients.containsKey(id);
    }

    public void clearAll() {
        clients.clear();
    }

    public void broadcast(String url, @NotNull Object obj) {
        clients.forEach((key, value) -> {
                    if (!value.isShield(this)) {
                        value.send(url, obj);
                    }
                }
        );
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
