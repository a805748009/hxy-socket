package com.business.entry;

/**
 * @Author 黄新宇
 * @Date 2018/5/29 下午7:58
 * @Description TODO 通用消息bean
 **/
public class Curren {
    private int intParam;

    private String stringParam;

    private boolean booleanParam;

    public int getIntParam() {
        return intParam;
    }

    public Curren setIntParam(int intParam) {
        this.intParam = intParam;
        return this;
    }

    public String getStringParam() {
        return stringParam;
    }

    public Curren setStringParam(String stringParam) {
        this.stringParam = stringParam;
        return this;
    }

    public boolean isBooleanParam() {
        return booleanParam;
    }

    public Curren setBooleanParam(boolean booleanParam) {
        this.booleanParam = booleanParam;
        return this;
    }
}
