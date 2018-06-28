package com.business.dao;

import com.business.entry.User;
import com.result.base.entry.backStageBean.UserCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.Map;


@Component
public interface UserDao {
    User loginById(@Param("userId") String userId);

    void updateGameUserCount(UserCount userCount);

    Map selectNowUserCount(@Param("gameName") String gameNmae);
}
