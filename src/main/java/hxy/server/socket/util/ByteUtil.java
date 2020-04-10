
package hxy.server.socket.util;


/***
 * @Description: byte工具类
 * @author hxy
 * @date 2020/4/9 15:02
 */
public class ByteUtil {

    /***
     * @Description: 合并数组
     * @author hxy
     * @date 2020/4/9 11:50
     */
    public static byte[] concat(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /***
     * @Description: 输出转字符串便于输出
     * @author hxy
     * @date 2020/4/9 11:50
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
}

