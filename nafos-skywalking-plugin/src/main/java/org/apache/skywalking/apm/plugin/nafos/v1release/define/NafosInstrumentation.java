package org.apache.skywalking.apm.plugin.nafos.v1release.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;


import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * {@link NafosInstrumentation} enhance the <code>route</code> method in <code>nafos.bootStrap.handle.http.HttpRouteHandle</code>
 * by <code>HandleInterceptor</code>
 *
 * @author hxy
 * maven 打包后获取target\skywalking-agent\plugins 下的jar丢入skywalking的plugins重新启动就可以加入nafos监控了。
 */
public class NafosInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "nafos.bootStrap.handle.http.HttpRouteHandle";
    private static final String NAFOS_METHOD_INTERCET_CLASS = "org.apache.skywalking.apm.plugin.nafos.v1release.NafosMethodInterceptor";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named("route");
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return NAFOS_METHOD_INTERCET_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }

}
