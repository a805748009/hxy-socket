package com.business.dao;

import com.result.base.entry.backStageBean.OnlineCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface OnlineDao {

    void addOnlineCount(OnlineCount onlineCount);

    List<Map> selectOnlineCountInfo(@Param("gameName") String gameName, @Param("time") String time);
}
