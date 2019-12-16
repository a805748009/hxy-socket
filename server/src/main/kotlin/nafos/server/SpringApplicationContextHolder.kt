package nafos.server

import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

/***
 *@Description springContext保存
 *@Author      xinyu.huang
 *@Time        2019/11/23 19:35
 */
@Component
object SpringApplicationContextHolder : ApplicationContextAware {

    private val logger = LoggerFactory.getLogger(SpringApplicationContextHolder::class.java)

    @Volatile
    private var context: ApplicationContext? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(context: ApplicationContext) {
        SpringApplicationContextHolder.context = context
    }

    @JvmStatic
    fun getContext(): ApplicationContext? {
        return context
    }

    @JvmStatic
    fun getSpringBean(beanName: String): Any? {
        return context?.getBean(beanName)
    }

    @JvmStatic
    fun <T> getSpringBeanForClass(clazz: Class<T>): T? {
        return context?.getBean(clazz)
    }

    @JvmStatic
    fun getBeanDefinitionNames(): Array<String> {
        return context!!.beanDefinitionNames
    }

    /***
     *@Description 获取或初始化springContext
     *@Author      xinyu.huang
     *@Time        2019/11/23 19:52
     */
    @JvmStatic
    fun getOrInitContext(clazz: Class<*>): ApplicationContext {
        synchronized(this.javaClass) {
            context?.also { logger.info("ApplicationContext is running") }
                    ?: run {
                        if (AnnotationUtils.findAnnotation<ComponentScan>(clazz, ComponentScan::class.java) == null) {
                            throw IllegalStateException("startup class [" + clazz.name + "] must be Annotation ComponentScan and choose scan package")
                        }
                        val annoContext = AnnotationConfigApplicationContext()
                        annoContext.register(clazz)
                        annoContext.refresh()
                        context = annoContext
                    }
            logger.info("context-init:$context")
        }
        return context!!
    }
}