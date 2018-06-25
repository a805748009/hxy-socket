package com.business.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DruidDao {
    List<Map> selectDruidInfoList();

    int deleteDruidInfoById(@Param("druidId") String id);
}
