package com.controller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.core.entry.ResultStatus;
import nafos.core.mode.filter.fInterface.AbstractHttpInterceptor;
import org.springframework.stereotype.Component;

@Component
public class InterceptorDemo extends AbstractHttpInterceptor {
    @Override
    public ResultStatus interptor(ChannelHandlerContext ctx, NsRequest req) {
        if("hello".equals(req.stringQueryParam("hello"))){
            return new ResultStatus(true);
        }else{
            return new ResultStatus(false, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
