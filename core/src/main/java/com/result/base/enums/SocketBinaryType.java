package com.result.base.enums;

/**
 * socket二进制解码方式
 */
public enum SocketBinaryType {

    BYTESTRING("byteString","proto二次转码"),
    PARENTFORBASESOCKETMESSAGE("parentForBaseSocketMessage","继承baseSocketMessage"),
    INTBEFORE("intBefore","前置int类型Id");


    private String type;
    private String desc;

    SocketBinaryType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}