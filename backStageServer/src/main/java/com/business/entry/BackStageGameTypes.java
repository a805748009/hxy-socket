package com.business.entry;

public class BackStageGameTypes {
    private int id;
    private int type;
    private String typeName;
    private String typeImgUrl;
    private int orderValue;

    public BackStageGameTypes(int id, int type, String typeName, String typeImgUrl, int orderValue) {
        this.id = id;
        this.type = type;
        this.typeName = typeName;
        this.typeImgUrl = typeImgUrl;
        this.orderValue = orderValue;
    }
    public BackStageGameTypes(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeImgUrl() {
        return typeImgUrl;
    }

    public void setTypeImgUrl(String typeImgUrl) {
        this.typeImgUrl = typeImgUrl;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    @Override
    public String toString() {
        return "BackStageGameTypes{" +
                "id=" + id +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", typeImgUrl='" + typeImgUrl + '\'' +
                ", orderValue=" + orderValue +
                '}';
    }
}
