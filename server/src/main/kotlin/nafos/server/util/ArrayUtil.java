
package nafos.server.util;

import java.lang.reflect.Array;

/**
 * ClassName:ArrayUtil
 * Function: TODO ADD FUNCTION.
 * Date:     2017年8月23日 下午1:57:12
 *
 * @author HXY
 */
public class ArrayUtil {


    /**
     * 合并数组
     *
     * @param array1
     * @param array2
     * @return
     */
    public static Object[] concat(Object[] array1, Object[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        } else {
            Object[] joinedArray = (Object[]) ((Object[]) Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length));
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);

            try {
                System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
                return joinedArray;
            } catch (ArrayStoreException var6) {
                Class type1 = array1.getClass().getComponentType();
                Class type2 = array2.getClass().getComponentType();
                if (!type1.isAssignableFrom(type2)) {
                    throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName());
                } else {
                    throw var6;
                }
            }
        }
    }


    /**
     * 合并数组
     *
     * @param data1
     * @param data2
     * @return
     */
    public static byte[] concat(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 输出转字符串便于输出
     */
    public static String byteArrayToString(byte[] bs) {
        StringBuffer buffer = new StringBuffer();
        for (int x = 0; x < bs.length; x++) {
            if (x == 0) {
                buffer.append("[" + bs[x]);
            } else if (x == bs.length - 1) {
                buffer.append("," + bs[x] + "]");
            } else {
                buffer.append("," + bs[x]);
            }
        }
        return buffer.toString();
    }

    /**
     * 判断数组中是否包含某个元素
     *
     * @author HXY
     * @date 2017年8月29日下午1:21:02
     */
    public static <T> boolean contains(T[] array, T obj) {
        return indexOf(array, obj) != -1;
    }


    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }


    public static byte[] longToByteArray(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long byteArrayToLong(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }


    public static Object[] clone(Object[] array) {
        return array == null ? null : (Object[]) ((Object[]) array.clone());
    }

    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        } else {
            if (startIndex < 0) {
                startIndex = 0;
            }

            int i;
            if (objectToFind == null) {
                for (i = startIndex; i < array.length; ++i) {
                    if (array[i] == null) {
                        return i;
                    }
                }
            } else {
                for (i = startIndex; i < array.length; ++i) {
                    if (objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }
}

