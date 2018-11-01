package nafos.core.Thread;

/**
 * @Author 黄新宇
 * @Date 2018/11/1 下午9:07
 * @Description TODO
 **/
public class Processors {

    private static int process = Runtime.getRuntime().availableProcessors();

    public static int getProcess() {
        return process;
    }

}
