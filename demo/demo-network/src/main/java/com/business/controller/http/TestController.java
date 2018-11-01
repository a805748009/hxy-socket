package com.business.controller.http;

import com.business.entry.Curren;
import com.business.entry.bean.User;
import com.business.service.TestService;
import io.netty.handler.codec.http.FullHttpRequest;
import com.mode.error.MyHttpResponseStatus;
import nafos.core.Thread.ThreadLocalHelper;
import nafos.core.annotation.controller.Controller;
import nafos.core.annotation.controller.Handle;
import nafos.core.annotation.controller.Request;
import nafos.core.util.CastUtil;
import nafos.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import java.text.ParseException;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/5/24 下午2:36
 * @Description  controller支持写法示例
 **/
@Controller
@Handle(uri = "/test")
public class TestController {
    @Autowired
    TestService testService;

    //-------------  以上是protostuff方式接收,客户端可采用protobuff ------------//

    @Handle(uri = "/protoStuff/get", method = "GET")
    public Object protoStuffGet() throws ParseException {
        String userId = SecurityUtil.getLoginUser(ThreadLocalHelper.getRequest().getNafosCookieId(), User.class).getUserId();
        return testService.protoStuffGet(userId);
    }

    @Handle(uri = "/protoStuff/get", method = "GET")
    public Object protoStuffGetAndUrlParam( @RequestParam("id") String id, @RequestParam("name") String name) throws ParseException {
        String userId = SecurityUtil.getLoginUser(ThreadLocalHelper.getRequest().getNafosCookieId(), User.class).getUserId();
        return testService.protoStuffGet(userId);
    }


    @Handle(uri = "/protoStuff/post", method = "POST",printLog = true)
    public Object protoStuffPost(User user) {
        String userId = SecurityUtil.getLoginUser(ThreadLocalHelper.getRequest().getNafosCookieId(), User.class).getUserId();
        return testService.protoStuffPost(user,userId);
    }

    @Handle(uri = "/protoStuff/post", method = "POST",printLog = true)
    public Object protoStuffPostAndUrlParam(User user, @RequestParam("id") String id) {
        String userId = SecurityUtil.getLoginUser(ThreadLocalHelper.getRequest().getNafosCookieId(), User.class).getUserId();
        return testService.protoStuffPost(user,userId);
    }




    //-------------  以下是JSON方式接收 ------------//

    @Handle(uri = "/json/get", method = "GET",type = "JSON")
    public Object jsonGet(Curren curren) {
        String userId = SecurityUtil.getLoginUser(ThreadLocalHelper.getRequest().getNafosCookieId(), User.class).getUserId();
        return curren;
    }

    @Handle(uri = "/json/getMap", method = "GET",type = "JSON",runOnWorkGroup = true)
    public Object jsonGetMap(Map map) {
        String id = CastUtil.castString(map.get("id"));
        return null;
    }

    @Handle(uri = "/json/post", method = "POST",type = "JSON",runOnWorkGroup = true)
    public Object jsonPost(Curren curren) {
        return curren;
    }

    @Handle(uri = "/json/postAndUrlParam", method = "POST",type = "JSON",runOnWorkGroup = true)
    public Object jsonPostAndUrlParam(Curren curren, @RequestParam("id") String id, @RequestParam("name") String name) {
        return MyHttpResponseStatus.SERVERSHUTDOWN;
    }


    @Handle(uri = "/json/getRequest", method = "POST",type = "JSON",runOnWorkGroup = true)
    public Object jsongetRequest(Curren curren, @RequestParam("id") String id, @Request FullHttpRequest fullHttpRequest) {
        System.out.println(fullHttpRequest.uri());
        return "123456";
    }

}
