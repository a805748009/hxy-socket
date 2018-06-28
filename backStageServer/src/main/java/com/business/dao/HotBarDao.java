package com.business.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;


@Component
public interface HotBarDao {

    int addGame(@Param("gameName") String gameName, @Param("imgUrl") String imgUrl, @Param("iconUrl") String iconUrl, @Param("appID") String appID, @Param("order") int order, @Param("gameIntro") String gameIntro);

    int deleteGame(@Param("id") String id);

    int updateGame(@Param("id") String id, @Param("gameName") String gameName, @Param("imgUrl") String imgUrl, @Param("iconUrl") String iconUrl, @Param("appID") String appID, @Param("order") int order, @Param("gameIntro") String gameIntro);

    List<Map> selectAllGame();

    List<Map> selectAllBanner();

    int updateBanner(@Param("id") String id, @Param("gameName") String gameName, @Param("appID") String appID, @Param("imgUrl") String imgUrl, @Param("order") int order);

    int deleteBanner(@Param("id") String id);

    int addBanner(@Param("gameName") String gameName, @Param("appID") String appID, @Param("imgUrl") String imgUrl, @Param("order") int order);
}
