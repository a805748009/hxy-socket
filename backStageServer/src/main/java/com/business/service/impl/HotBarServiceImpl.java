package com.business.service.impl;

import com.business.dao.HotBarDao;
import com.business.service.HotBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HotBarServiceImpl implements HotBarService {
    @Autowired
    HotBarDao hotBarDao;

    @Override
    public int addGame(String gameName, String imgUrl, String iconUrl, String appID, int order, String gameIntro) {
        return hotBarDao.addGame(gameName,imgUrl,iconUrl,appID,order,gameIntro);
    }

    @Override
    public int deleteGame(String id) {
        return hotBarDao.deleteGame(id);
    }

    @Override
    public int updateGame(String id, String gameName, String imgUrl, String iconUrl, String appID, int order, String gameIntro) {
        return hotBarDao.updateGame(id,gameName,imgUrl,iconUrl,appID,order,gameIntro);
    }

    @Override
    public List<Map> selectAllGameList() {
        return hotBarDao.selectAllGame();
    }

    @Override
    public int addBanner(String gameName, String appID, String imgUrl, int order) {
        return hotBarDao.addBanner(gameName,appID,imgUrl,order);
    }

    @Override
    public int deleteBanner(String id) {
        return hotBarDao.deleteBanner(id);
    }

    @Override
    public int updateBanner(String id, String gameName, String appID, String imgUrl, int order) {
        return hotBarDao.updateBanner(id,gameName,appID,imgUrl,order);
    }

    @Override
    public List<Map> selectAllBannerList() {
        return hotBarDao.selectAllBanner();
    }
}
