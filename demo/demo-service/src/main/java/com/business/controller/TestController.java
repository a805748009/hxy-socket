package com.business.controller;

import com.business.entry.Curren;
import com.business.entry.bean.User;
import com.business.entry.view.HomeView;
import com.business.service.UserService;
import nafos.core.annotation.controller.Controller;
import nafos.core.annotation.controller.Handle;
import nafos.core.annotation.controller.Request;
import nafos.core.annotation.rpc.RemoteCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Handle(uri = "/test")
@RemoteCall
public class TestController {

    @Autowired
    UserService userService;

    @Handle(uri = "/protoStuffGet", method = "GET",printLog = true)
    public Object protoStuffGet(@RequestParam("userId") String userId) {
        return new HomeView().setCurren(new Curren().setIntParam(1));
    }

    @Handle(uri = "/protoStuffPost", method = "POST",printLog = true)
    public Object protoStuffPost(User user, @RequestParam("userId") String userId) {
        return new HomeView().setCurren(new Curren().setIntParam(1));

    }

}
