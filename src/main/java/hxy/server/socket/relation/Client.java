package hxy.server.socket.relation;

import hxy.server.socket.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName Client
 * @Description channel方法扩展
 * @Author hxy
 * @Date 2020/4/9 15:16
 */
public class Client implements AttributeMap {
    public static String CLIENT_ATTRIBUTE_KEY = "CLIENT";

    private String id;

    private Channel channel;

    private final ConcurrentHashMap<String, Namespace> namespaces = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    private final CopyOnWriteArraySet<ClientContext> shieldContexts = new CopyOnWriteArraySet();


    public Client(String id, Channel channel) {
        this.id = id;
        this.channel = channel;
        Global.INSTANCE.addClient(this);
        this.attr(AttributeKey.valueOf(CLIENT_ATTRIBUTE_KEY)).set(this);
    }

    public Channel getChannel() {
        return channel;
    }

    public String getId() {
        return id;
    }

    public void close() {
        Global.INSTANCE.removeClient(this);
        namespaces.values().forEach(ns -> ns.removeClient(this));
        rooms.values().forEach(rm -> rm.removeClient(this));
        channel.close();
    }


    /***
     * @Description: 发送消息
     * @author hxy
     * @date 2020/4/9 16:32
     */
    public void send(String url, @NotNull Object obj) {
        if (!channel.isActive()) {
            return;
        }
        channel.writeAndFlush(url + "|" + JsonUtil.toJson(obj));
    }

    public void joinRoom(@NotNull Room room) {
        room.addClient(this);
        rooms.put(room.id, room);
    }

    public void joinRoom(@NotNull String namespaceId, @NotNull String roomId) {
        if (!namespaces.containsKey(namespaceId)) {
            joinNamespace(Global.INSTANCE.getNamespace(namespaceId));
        }
        Room room = namespaces.get(namespaceId).getRoom(roomId);
        Objects.requireNonNull(room, String.format("room: {} not exists in namespace:{}", roomId, namespaceId));
        joinRoom(room);
    }

    public Room getRoom(@NotNull String roomId) {
        return rooms.get(roomId);
    }

    public boolean leaveRoom(@NotNull String roomId){
        Room room = rooms.remove(roomId);
        if(room == null){
            return false;
        }
        room.removeClient(this);
        return true;
    }

    public ConcurrentHashMap<String, Namespace> getNamespaces() {
        return namespaces;
    }

    public ConcurrentHashMap<String, Room> getRooms() {
        return rooms;
    }

    public void joinNamespace(@NotNull Namespace namespace) {
        namespace.addClient(this);
        namespaces.put(namespace.id, namespace);
    }

    public void joinNamespace(@NotNull String namespaceId) {
        Namespace namespace = Global.INSTANCE.getNamespace(namespaceId);
        Objects.requireNonNull(namespace, "namespace not exists :" + namespaceId);
        joinNamespace(namespace);
    }

    public Namespace getNamespace(@NotNull String namespaceId) {
        return namespaces.get(namespaceId);
    }

    public boolean leaveNamespace(@NotNull String namespaceId) {
        Namespace namespace = namespaces.remove(namespaceId);
        if (namespace == null){
            return false;
        }
        namespace.removeClient(this);
        return true;
    }

    /***
     * @description: 频闭某个房间
     * @author hxy
     * @date 2020/11/13 14:13
     */
    public void shieldContext(@NotNull ClientContext clientContext){
        this.shieldContexts.add(clientContext);
    }

    public boolean isShield(@NotNull ClientContext clientContext){
        return shieldContexts.contains(clientContext);
    }


    @Override
    public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
        return channel.attr(attributeKey);
    }

    @Override
    public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
        return channel.hasAttr(attributeKey);
    }

}
