package com.business.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AdervtisingDao {
    int addAdvertising(@Param("dataBaseName") String dataBaseName, @Param("buttonImg") String buttonImg, @Param("groundImg") String groundImg, @Param("comment") String comment);

    int deleteAdvertising(@Param("dataBaseName") String dataBaseName, @Param("id") String id);

    int updateAvertising(@Param("dataBaseName") String dataBaseName, @Param("id") String id, @Param("buttonImg") String buttonImg, @Param("groundImg") String groundImg, @Param("comment") String comment);

    List<Map> selectAllAvertising(@Param("dataBaseName") String dataBaseName);
}
