package com.business.service.impl;

import com.business.dao.AdervtisingDao;
import com.business.service.AdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdvertisingServerImpl implements AdvertisingService {
    @Autowired
    AdervtisingDao adervtisingDao;

    private static String dataBaseName="noBody";

    @Override
    public int addAdvertising(String buttonImg, String groundImg,String comment) {
        return adervtisingDao.addAdvertising(dataBaseName,buttonImg,groundImg,comment);
    }

    @Override
    public int deleteAdvertising(String id) {
        return adervtisingDao.deleteAdvertising(dataBaseName,id);
    }

    @Override
    public int updateAvertising(String id, String buttonImg, String groundImg,String comment) {
        return adervtisingDao.updateAvertising(dataBaseName,id,buttonImg,groundImg,comment);
    }

    @Override
    public List<Map> selectAllAvertising() {
        return adervtisingDao.selectAllAvertising(dataBaseName);
    }
}
