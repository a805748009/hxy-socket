package com.business.entry.view;

import com.business.entry.Curren;
import com.business.entry.bean.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 黄新宇
 * @Date 2018/5/24 下午3:50
 * @Description TODO
 **/
public class HomeView implements Serializable{


    private static final long serialVersionUID = 5246675889970032698L;

    private int fashionId;

    private boolean isOnHome;

    private boolean isHavePlantBoss;



    private Curren curren;

    private User user;




    public HomeView() {
    }

    public int getFashionId() {
        return fashionId;
    }

    public void setFashionId(int fashionId) {
        this.fashionId = fashionId;
    }

    public boolean isOnHome() {
        return isOnHome;
    }

    public void setOnHome(boolean onHome) {
        isOnHome = onHome;
    }

    public boolean isHavePlantBoss() {
        return isHavePlantBoss;
    }

    public void setHavePlantBoss(boolean havePlantBoss) {
        isHavePlantBoss = havePlantBoss;
    }

    public Curren getCurren() {
        return curren;
    }

    public HomeView setCurren(Curren curren) {
        this.curren = curren;
        return this;
    }

    public User getUser() {
        return user;
    }

    public HomeView setUser(User user) {
        this.user = user;
        return this;
    }
}
