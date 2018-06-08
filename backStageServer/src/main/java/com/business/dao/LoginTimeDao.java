package com.business.dao;

import com.result.base.entry.backStageBean.LoginTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface LoginTimeDao {

    void addLoginTime(LoginTime loginTime);

    List<Map> selectSevenDayLoginTime(@Param("gameName") String gameName, @Param("time") String time);

    List<Map> selectLoginTimenInterval(@Param("gameName") String gameName, @Param("time") String time);

    int selectOnlineMaxCountByDay(@Param("day") String day, @Param("gameName") String gameName);
}
