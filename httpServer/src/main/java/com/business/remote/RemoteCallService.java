package com.business.remote;

import com.business.bean.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author 黄新宇
 * @Date 2018/6/11 下午5:17
 * @Description TODO
 **/
@FeignClient(value = "hlo",path ="/bcRemoteCall" )
public interface RemoteCallService {
    @RequestMapping(value = "/test",method = RequestMethod.POST,consumes = "application/json")
    String remoteCall(User u);
}

