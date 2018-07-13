package com.hxy.nettygo.result.base.tools;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * @Author 黄新宇
 * @Date 2018/7/9 下午4:16
 * @Description 热更新工具类，直接调用，传入热更新的agent的jar包绝对路径即可
 **/
public class HotloadUtil {
    public static void runHotLoad(String hotLoadJarPath) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            VirtualMachine vm = VirtualMachine.attach(pid);
            // 这个路径是相对于被热更的服务的，也就是这个pid的服务，也可以使用绝对路径。
            vm.loadAgent(hotLoadJarPath);
            vm.detach();
    }
}
