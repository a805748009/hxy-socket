package test;

import hxy.server.socket.anno.EnableCodeHandler;
import hxy.server.socket.anno.EnableTcpsocket;
import hxy.server.socket.anno.EnableWebsocket;
import hxy.server.socket.entity.SslInfo;
import hxy.server.socket.relation.Global;
import hxy.server.socket.relation.Room;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @ClassName RunApp
 * @Description TODO
 * @Author hxy
 * @Date 2020/4/8 17:04
 */
@SpringBootApplication
@EnableTcpsocket
public class RunApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RunApp.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
