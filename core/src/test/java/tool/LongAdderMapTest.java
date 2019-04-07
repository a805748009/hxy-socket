package tool;

import nafos.core.monitor.RunWatch;

import java.util.Random;

/**
 * @Classname LongAdderMapTest
 * @Description TODO
 * @Date 2019/2/28 19:09
 * @Created by xinyu.huang
 */
public class LongAdderMapTest {
    public static void main(String[] args) throws InterruptedException {


//        RunWatch r2 = RunWatch.init();
//        r2.run();
//
//        r2.stop();
//
//        RunWatch r3 = RunWatch.init();
//        r3.run();
//        new Thread(()->{
//            RunWatch.resetInfo();
//        }).start();
//        Thread.sleep(2000);
//        r3.stop();
//        RunWatch.print();
//        RunWatch.resetInfo();
//        Thread.sleep(2000);
//        RunWatch.print();

        long s = System.currentTimeMillis();
        int i=0;
        while(i<1000000){
            RunWatch r2 = RunWatch.init();
            r2.run();
            long m = System.currentTimeMillis();
            r2.stop();
            i++;
        }
        long e = System.currentTimeMillis();
        System.out.println(e-s);

    }
}
