package com.business.dao;

import com.result.base.entry.backStageBean.JVMmemory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface MemoryDao {

    void addMemory(JVMmemory jvMmemory);

    List<Map> selectJVMInfoByNameAndTime(@Param("gameName") String gameName, @Param("time") String time);
}
