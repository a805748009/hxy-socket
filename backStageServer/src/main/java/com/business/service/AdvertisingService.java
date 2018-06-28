package com.business.service;

import java.util.List;
import java.util.Map;

public interface AdvertisingService {

    int addAdvertising(String buttonImg, String groundImg, String comment);

    int deleteAdvertising(String id);

    int updateAvertising(String id, String buttonImg, String groundImg, String comment);

    List<Map> selectAllAvertising();
}
