package com.business.dao;

import com.business.entry.BackStageGameTypes;
import com.business.entry.BackStageGames;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;


@Component
public interface HotBarDao {

    int addGame(@Param("gameName") String gameName, @Param("imgUrl") String imgUrl, @Param("iconUrl") String iconUrl, @Param("appID") String appID, @Param("order") int order, @Param("gameIntro") String gameIntro);

    int deleteGame(@Param("id") String id);

    int updateGame(@Param("id") String id, @Param("gameName") String gameName, @Param("imgUrl") String imgUrl, @Param("iconUrl") String iconUrl, @Param("appID") String appID, @Param("order") int order, @Param("gameIntro") String gameIntro);

    List<BackStageGames> selectAllGame();

    List<Map> selectAllBanner();

    int updateBanner(@Param("id") String id, @Param("gameName") String gameName, @Param("appID") String appID, @Param("imgUrl") String imgUrl, @Param("order") int order);

    int deleteBanner(@Param("id") String id);

    int addBanner(@Param("gameName") String gameName, @Param("appID") String appID, @Param("imgUrl") String imgUrl, @Param("order") int order);

    List<BackStageGameTypes> selectAllTypes();

    List<BackStageGames> selectGamesByType(@Param("type") String type);
}
