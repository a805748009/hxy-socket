package test.code.handler;

import hxy.server.socket.anno.EnableCodeHandler;
import hxy.server.socket.anno.EnableWebsocket;
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
@EnableWebsocket
@EnableCodeHandler
public class RunApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RunApp.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
