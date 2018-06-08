package com.business.dao;

import com.result.base.entry.backStageBean.Admin;
import com.result.base.entry.backStageBean.UserCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserDao {
    Admin loginById(@Param("userId") String userId);

    void updateGameUserCount(UserCount userCount);

    Map selectNowUserCount(@Param("gameName") String gameNmae);
}
