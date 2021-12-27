package hxy.server.socket.relation;


/**
 * @ClassName Room
 * @Description 房间操作
 * @Author hxy
 * @Date 2020/4/9 15:12
 */
public class Room extends ClientContext {
    private Namespace namespace;

    public Room(String namespaceId, String id) {
        super(id);
        namespace = Global.INSTANCE.getNamespace(namespaceId);
        if(namespace == null){
            synchronized (Namespace.class){
                if((namespace = Global.INSTANCE.getNamespace(namespaceId)) == null){
                    namespace = new Namespace(namespaceId);
                }
            }
        }
        namespace.addRoom(this);
    }

    public Namespace getNamespace() {
        return namespace;
    }
}
