package com.mode.error;

import io.netty.handler.codec.http.HttpResponseStatus;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月28日 下午8:17:06 
* 类说明 
*/
public class MyHttpResponseStatus extends HttpResponseStatus{
	
	
	public MyHttpResponseStatus(int code, String reasonPhrase) {
		super(code, reasonPhrase);
	}

	public static final MyHttpResponseStatus ILLEGAL = new MyHttpResponseStatus(799, "xjb play");

	public static final MyHttpResponseStatus SERVERSHUTDOWN = new MyHttpResponseStatus(800, "server update");

	public static final MyHttpResponseStatus LOGIN_ANOTHER = new MyHttpResponseStatus(801, "login other where");

	public static final MyHttpResponseStatus NUMBER_NOT_ENGOUTH = new MyHttpResponseStatus(802, "numbers not enough");

	//种植系统
	public static final MyHttpResponseStatus PLANT_HAD = new MyHttpResponseStatus(821, "plant others");

	public static final MyHttpResponseStatus PLANT_NO_MATURE = new MyHttpResponseStatus(822, "plant no fruit");

	public static final MyHttpResponseStatus BE_LEFT_TOO_LITTLE = new MyHttpResponseStatus(823, "fruit be less");

	public static final MyHttpResponseStatus PLANT_FAILD = new MyHttpResponseStatus(824, "seed not enough");

	public static final MyHttpResponseStatus PLANT_STEAL_FAILD = new MyHttpResponseStatus(825, "steal enough");

	public static final MyHttpResponseStatus PLANT_BOSS_ATTACKED = new MyHttpResponseStatus(826, "attack boss failed");

	//好友系统
	public static final MyHttpResponseStatus USER_NOT_FOUND = new MyHttpResponseStatus(830, "user undifend");
	public static final MyHttpResponseStatus FRIEND_TO_MANY = new MyHttpResponseStatus(831, "friend enough");
	public static final MyHttpResponseStatus SENDED_FRIEND_REQUEST = new MyHttpResponseStatus(832, "request friend ed");

	//探索
	public static final MyHttpResponseStatus EXPLORE_NOT_HAVEITEM = new MyHttpResponseStatus(840, "explore must have food or drink");
	public static final MyHttpResponseStatus EXPLORE_HAD_TOGO = new MyHttpResponseStatus(841, "is going explore");

	//天赋点
	public static final MyHttpResponseStatus TALENT_NOT_FILTER = new MyHttpResponseStatus(850, "must filter things");

	//合成
	public static final MyHttpResponseStatus NOT_COMPOSE = new MyHttpResponseStatus(861, "cant't compose");


	//商店
	public static final MyHttpResponseStatus SHOP_NO_ITEM = new MyHttpResponseStatus(870, "no shops to buy");
	public static final MyHttpResponseStatus SHOP_NO_SELL_ITEM = new MyHttpResponseStatus(871, "item not can sell");
	 
}
