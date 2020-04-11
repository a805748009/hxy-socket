package hxy.server.socket.engine;

import com.esotericsoftware.reflectasm.MethodAccess;
import hxy.server.socket.anno.Socket;
import hxy.server.socket.engine.factory.CodeHandlerRouteFactory;
import hxy.server.socket.entity.CodeHandlerBean;
import hxy.server.socket.relation.Client;
import hxy.server.socket.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/8 21:32
 */
@Socket
public class SimpleCodeHandler implements SocketMsgHandler {
    private Logger logger = LoggerFactory.getLogger(SimpleCodeHandler.class);

    @Override
    public void onConnect(ChannelHandlerContext ctx, HttpRequest req) {
        logger.debug("新的连接:{}", ctx.channel().toString());
        for (ChannelActive channelActive : CodeHandlerRouteFactory.getChannelActives()) {
            channelActive.onConnect(ctx, req);
        }
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {
        if (msg.isEmpty() || msg.trim().isEmpty()) {
            return;
        }
        String clientCode = msg.substring(0, msg.indexOf("|"));
        String surplusStr = msg.substring(msg.indexOf("|") + 1);
        String serverCode = surplusStr.substring(0, surplusStr.indexOf("|"));

        CodeHandlerBean codeHandlerBean = CodeHandlerRouteFactory.getCodeHandlerBean(Integer.parseInt(serverCode));
        if (codeHandlerBean == null) {
            logger.warn("serverCode:{} is not exists", serverCode);
            return;
        }

        String contentJsonStr = surplusStr.substring(surplusStr.indexOf("|") + 1);

        Class<?>[] clzs = codeHandlerBean.getParamType();
        Object[] params = new Object[clzs.length];
        for (int i = 0; i < clzs.length; i++) {
            Class parameter = clzs[i];
            if (Channel.class.isAssignableFrom(parameter)) {
                params[i] = ctx.channel();
            } else if (Client.class.isAssignableFrom(parameter)) {
                Attribute<Client> attribute = ctx.channel().attr(AttributeKey.valueOf(Client.CLIENT_ATTRIBUTE_KEY));
                Client client = attribute.get();
                params[i] = client;
            } else if (int.class.isAssignableFrom(parameter) || Integer.class.isAssignableFrom(parameter)) {
                params[i] = Integer.parseInt(clientCode);
            } else if (Map.class.isAssignableFrom(parameter)) {
                params[i] = JsonUtil.jsonToMap(contentJsonStr);
            } else {
                params[i] = JsonUtil.json2Object(contentJsonStr, parameter);
            }

        }
        MethodAccess methodAccess = codeHandlerBean.getMethod();
        methodAccess.invoke(codeHandlerBean.getBean(), codeHandlerBean.getIndex(), params);
    }

    @Override
    public void disConnect(ChannelHandlerContext ctx) {
        logger.debug("断开连接:{}", ctx.channel().toString());
        for (ChannelActive channelActive : CodeHandlerRouteFactory.getChannelActives()) {
            channelActive.disConnect(ctx);
        }
    }

}
