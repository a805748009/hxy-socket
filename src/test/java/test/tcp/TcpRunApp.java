package test.tcp;

import hxy.server.socket.anno.EnableTcpsocket;
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
public class TcpRunApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TcpRunApp.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
