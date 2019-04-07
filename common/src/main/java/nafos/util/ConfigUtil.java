
package nafos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * ClassName:ConfigUtil Function: TODO ADD FUNCTION. Date: 2017年11月1日 下午2:57:01
 *
 * @author HXY
 */
public class ConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    private static Properties serverProps = null;

    static {
        serverProps = getProperties("application");
    }

    /**
     * 获取配置文件类
     *
     * @param propsPath
     * @return
     * @author HXY
     * @date 2017年11月1日下午3:00:53
     */
    public static Properties getProperties(String propsPath) {
        Properties props = new Properties();
        InputStream is = null;
        try {
            if (propsPath == null || "".equals(propsPath)) {
                throw new IllegalArgumentException();
            }
            String suffix = ".properties";
            if (propsPath.lastIndexOf(suffix) == -1) {
                propsPath = propsPath + suffix;
            }
            is = ClassLoader.getSystemResourceAsStream(propsPath);
            if (is != null) {
                props.load(is);
                return props;
            }
        } catch (Exception e) {
            logger.error("加载属性文件出错！", e);
            throw new IllegalStateException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("释放资源出错！", e);
            }
        }
        return null;
    }

    /**
     * 获取值
     *
     * @param propsPath
     * @param key
     * @return
     * @author HXY
     * @date 2017年11月1日下午3:30:27
     */
    public static String getValue(String propsPath, String key) {
        return getProperties(propsPath).getProperty(key);
    }

    public static String getServerValue(String key) {
        return serverProps.getProperty(key);
    }

    /**
     * 获取值
     *
     * @param propsPath
     * @param key
     * @return
     * @author HXY
     * @date 2017年11月1日下午3:30:27
     */
    public static String getValue(String propsPath, String key, String defaultvalue) {
        String value = getProperties(propsPath).getProperty(key);
        if (!"".equals(value) && value != null) {
            return value;
        }
        return defaultvalue;
    }

    public static String getServerValue(String key, String defaultvalue) {
        String value = serverProps.getProperty(key);
        if (!"".equals(value) && value != null) {
            return value;
        }
        return defaultvalue;
    }


    /**
     * @param @param  path
     * @param @return 设定文件
     * @return ResourceBundle    返回类型
     * @throws
     * @author huangxinyu
     * @version 创建时间：2018年3月20日 上午9:18:52
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static ResourceBundle getBundle(String path) {
        ResourceBundle rb = null;
        BufferedInputStream inputStream;
        String proFilePath = System.getProperty("user.dir") + path;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(proFilePath));
            rb = new PropertyResourceBundle(inputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rb;
    }


}
