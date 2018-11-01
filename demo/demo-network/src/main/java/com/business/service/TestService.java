package com.business.service;

import com.business.entry.bean.User;
import com.business.entry.view.HomeView;
import com.mode.configuration.ProtoFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feignClient 调用   ，本例主要演示采用protostuff优化的操作， 传统JSON操作去除configuration值即可，详情参考官网，本文不做赘述
 */
@FeignClient(value = "service",path ="/nafosRemoteCall/test" , configuration = ProtoFeignConfiguration.class)
public interface TestService {

    @RequestMapping(value = "/protoStuffGet",method = RequestMethod.GET)
    HomeView protoStuffGet(@RequestParam("userId") String userId);

    @RequestMapping(value = "/protoStuffPost",method = RequestMethod.POST)
    HomeView protoStuffPost(@RequestBody User user,@RequestParam("userId") String userId);


}
