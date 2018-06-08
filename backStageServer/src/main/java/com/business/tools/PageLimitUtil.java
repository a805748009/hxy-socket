package com.business.tools;

import com.result.base.tools.ObjectUtil;

import java.util.Map;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月23日 下午1:43:54 
* 类说明 
*/
public class PageLimitUtil {
	
	/**
	 * 分页工具
	 * @param map
	 * @return
	 */
	public static Map<String,Object> pageHelper(Map<String,Object> map){
		if(ObjectUtil.isNull(map.get("pageNum"))||ObjectUtil.isNull(map.get("pageSize")))
			return map;
		
		map.put("limitStart", ((Integer)(map.get("pageNum"))-1)*
				(Integer)(map.get("pageSize")));
		map.put("limitEnd", map.get("pageSize"));
		return map;
	}

}
