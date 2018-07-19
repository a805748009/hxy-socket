package com.business.entry;

public class BackStageGames {
    private int id;
    private String gameName;
    private String imgUrl;
    private String appid;
    private String gameIntroduct;
    private String iconUrl;
    private int orderValue;
    private int type;
    private String comentUrl;//弹出的网页地址

    public BackStageGames(int id, String gameName, String imgUrl, String appid, String gameIntroduct, String iconUrl, int orderValue, int type, String comentUrl) {
        this.id = id;
        this.gameName = gameName;
        this.imgUrl = imgUrl;
        this.appid = appid;
        this.gameIntroduct = gameIntroduct;
        this.iconUrl = iconUrl;
        this.orderValue = orderValue;
        this.type = type;
        this.comentUrl = comentUrl;
    }

    public BackStageGames() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getGameIntroduct() {
        return gameIntroduct;
    }

    public void setGameIntroduct(String gameIntroduct) {
        this.gameIntroduct = gameIntroduct;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getComentUrl() {
        return comentUrl;
    }

    public void setComentUrl(String comentUrl) {
        this.comentUrl = comentUrl;
    }

    @Override
    public String toString() {
        return "BackStageGames{" +
                "id=" + id +
                ", gameName='" + gameName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", appid='" + appid + '\'' +
                ", gameIntroduct='" + gameIntroduct + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", orderValue=" + orderValue +
                ", type=" + type +
                ", comentUrl=" + comentUrl +
                '}';
    }
}
