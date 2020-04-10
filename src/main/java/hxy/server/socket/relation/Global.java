package hxy.server.socket.relation;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public class Global extends ClientContext {

    public static Global INSTANCE = new Global("GLOBAL");

    private final ConcurrentHashMap<String, Namespace> namespaces = new ConcurrentHashMap<>(1024);

    public Global(String id) {
        super(id);
    }

    public int roomCount() {
        return namespaces.values().stream().mapToInt(Namespace::roomCount).sum();
    }

    public void addNamespace(@NotNull Namespace namespace) {
        namespaces.put(namespace.id, namespace);
    }

    public Namespace getNamespace(String id) {
        return namespaces.get(id);
    }

    public int namespaceCount() {
        return namespaces.size();
    }

}
