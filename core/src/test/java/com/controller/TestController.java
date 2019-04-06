package com.controller;

import nafos.core.annotation.Controller;
import nafos.core.annotation.http.Get;
import nafos.core.annotation.http.RequestParam;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.bootStrap.handle.http.NsRespone;

@Controller(interceptor = InterceptorDemo.class)
public class TestController {


    @Get(uri = "/test")
    public Object getHello(@RequestParam("cs") String cs, NsRequest nsRequest, NsRespone nsRespone){
        nsRespone.setCookie("jsessionId","123456");
//        System.out.println(cs);
//        System.out.println(nsRequest.uri());
        return "456";
    }

}
