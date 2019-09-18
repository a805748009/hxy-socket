package nafos;

import java.util.*;

/**
 * @Author 黄新宇
 * @Date 2018/6/12 上午9:31
 * @Description TODO
 **/
public class CdkMakeUtil {

    /**
     * 自定义进制(0,1没有加入,容易与o,l混淆)
     */
    private static final char[] r = new char[]{'Q', 'W', 'E', '8', 'A', 'S', '2', 'D', 'Z', 'X', '9', 'C', '7', 'P', '5', 'I', 'K', '3', 'M', 'J', 'U', 'F', 'R', '4', 'V', 'Y', 'L', 'T', 'N', '6', 'B', 'G', 'H'};

    /**
     * (不能与自定义进制有重复)
     */
    private static final char b = 'o';

    /**
     * 进制长度
     */
    private static final int binLen = r.length;

    /**
     * 序列最小长度
     */
    private static final int s = 6;

    /**
     * 根据ID生成六位随机码
     *
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        char[] buf = new char[32];
        int charPos = 32;

        while ((id / binLen) > 0) {
            int ind = (int) (id % binLen);
            // System.out.println(num + "-->" + ind);
            buf[--charPos] = r[ind];
            id /= binLen;
        }
        buf[--charPos] = r[(int) (id % binLen)];
        // System.out.println(num + "-->" + num % binLen);
        String str = new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if (str.length() < s) {
            StringBuilder sb = new StringBuilder();
            sb.append(b);
            Random rnd = new Random();
            for (int i = 1; i < s - str.length(); i++) {
                sb.append(r[rnd.nextInt(binLen)]);
            }
            str += sb.toString();
        }
        return str;
    }

    public static long codeToId(String code) {
        char chs[] = code.toCharArray();
        long res = 0L;
        for (int i = 0; i < chs.length; i++) {
            int ind = 0;
            for (int j = 0; j < binLen; j++) {
                if (chs[i] == r[j]) {
                    ind = j;
                    break;
                }
            }
            if (chs[i] == b) {
                break;
            }
            if (i > 0) {
                res = res * binLen + ind;
            } else {
                res = ind;
            }
            // System.out.println(ind + "-->" + res);
        }
        return res;
    }

    /**
     * @Desc     生成CDK的个数list
     * @Author   hxy
     * @Time     2019/9/18 18:16
     */
    public static List<String> makeCdk(int cdkNum) {
        Set<Integer> set = new LinkedHashSet();
        List<String> cdkList = new ArrayList<>();
        while (set.size() < cdkNum) {
            set.add(new Random().nextInt(9999999));
        }
        set.forEach(num -> {
            cdkList.add(toSerialCode(num));
        });
        return cdkList;
    }

    /**
     * @Desc     插库sql生成
     * @Author   hxy
     * @Time     2019/9/18 18:16
     */
    public static void main(String[] args) {
        List<String> li = makeCdk(30);
        StringBuilder sql = new StringBuilder("insert into cdk (cdk,type,num,rewardId) values ");
        int i = 0;
        for (String str : li) {
            System.out.println(str);
            if (i == 0) {
                sql.append("('" + str + "',1,1,'CDKExchange_3')");
            } else {
                sql.append(",('" + str + "',1,1,'CDKExchange_3')");
            }
            i++;
        }
        ;
        System.out.println(sql.toString());

    }


}
