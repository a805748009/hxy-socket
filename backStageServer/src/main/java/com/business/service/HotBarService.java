package com.business.service;

import java.util.List;
import java.util.Map;

public interface HotBarService {
    int addGame(String gameName, String imgUrl, String iconUrl, String appID, int order, String gameIntro);

    int deleteGame(String id);

    int updateGame(String id, String gameName, String imgUrl, String iconUrl, String appID, int order, String gameIntro);

    List<Map> selectAllGameList();

    int addBanner(String gameName, String appID, String imgUrl, int order);

    int deleteBanner(String id);

    int updateBanner(String id, String gameName, String appID, String imgUrl, int order);

    List<Map> selectAllBannerList();
}
