package com.business.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface DruidDao {
    List<Map> selectDruidInfoList();

    int deleteDruidInfoById(@Param("druidId") String id);
}
