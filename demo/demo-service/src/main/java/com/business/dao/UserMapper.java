package com.business.dao;


import com.business.entry.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {
	

	User getUserByOpenId(String openId);




}
