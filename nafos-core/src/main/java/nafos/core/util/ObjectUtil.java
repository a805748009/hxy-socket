
package nafos.core.util;

import java.util.Collection;

/**
 * ClassName:ObjectUtil Function: TODO ADD FUNCTION. Date: 2017年8月14日 下午6:12:41
 *
 * @author huangxinyu
 */
public class ObjectUtil {
    /**
     * 判断object不为空
     *
     * @param obj
     * @return
     * @author HXY
     * @date 2017年8月14日下午6:14:09
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * 判断object为空
     *
     * @param obj
     * @return
     * @author HXY
     * @date 2017年8月14日下午6:14:09
     */
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof String)) {
            return "".equals(((String) obj).trim());
        }
        if ((obj instanceof Object[])) {
            return ((Object[]) obj).length == 0;
        }
        if ((obj instanceof Collection)) {
            return ((Collection) obj).isEmpty();
        }
        return false;
    }
}
