package com.business.dao;

import com.hxy.nettygo.result.base.entry.backStageBean.Admin;
import com.hxy.nettygo.result.base.entry.backStageBean.UserCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public interface AdminDao {
    Admin selectLoginById(@Param("userId") String userId);

    void updateGameUserCount(UserCount userCount);

    Map selectNowUserCount(@Param("gameName") String gameNmae);
}
