package com.testRabbit;

import nafos.core.annotation.controller.Handle;
import nafos.core.entry.ClassAndMethod;
import nafos.msgQueue.annotation.RabbitListener;

/**
 * @Author 黄新宇
 * @Date 2018/11/7 下午7:12
 * @Description TODO
 **/
@RabbitListener
public class RabbitHandle {

    @Handle(code = 123)
    public void testListener(ClassAndMethod classAndMethod){
        System.out.println("----"+classAndMethod.getIndex());
    }
}
