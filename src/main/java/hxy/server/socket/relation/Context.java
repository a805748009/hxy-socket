package hxy.server.socket.relation;


import io.netty.util.AttributeMap;
import io.netty.util.DefaultAttributeMap;

public class Context {
    protected String id;
    /**
     * 属性
     */
    protected final AttributeMap attribute = new DefaultAttributeMap();

    public Context(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributeMap getAttribute() {
        return attribute;
    }

}
