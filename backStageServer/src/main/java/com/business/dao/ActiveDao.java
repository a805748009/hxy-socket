package com.business.dao;

import com.result.base.entry.backStageBean.ActiveCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ActiveDao {
    void addActive(ActiveCount activeCount);

    List<ActiveCount> selectSevenDayActiveCount(@Param("gameName") String gameName, @Param("time") String time);
}
