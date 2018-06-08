package com.result.base.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IoCache {

   
    //RoomId--->>>Room
    public static final Map<String,Room> roomMap = new ConcurrentHashMap<>();
    
    //根据nameSpace分组client
    public static final Map<String,HashSet<Client>> spaceClientMap = new ConcurrentHashMap<>();
	 
    //根据nameSpace分组room
    public static final Map<String,HashSet<String>> spaceRoomMap = new ConcurrentHashMap<>();
    
}