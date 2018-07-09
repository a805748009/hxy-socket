package com;


import com.tools.JarToolUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 黄新宇
 * @Date 2018/7/9 下午2:42
 * @Description 热更新，使用时请将需要更新的class文件带目录结构和此jar包丢在同一目录下，操作会重新加载所有的class文件。
 **/
public class HotLoad {
    private static List<String> classPathList = new ArrayList<String>();


    public static void agentmain(String args, Instrumentation inst) throws Exception {

        System.out.println("==========>>>>>>>>>>准备启动热更新");


        traverseFolder2(JarToolUtil.getJarDir());

        Class<?>[] allClass = inst.getAllLoadedClasses();
        for (Class<?> c : allClass) {
            for(String classStr:classPathList){
                if (c.getName().equals(classStr.replace(JarToolUtil.getJarDir()+"/","").replace("/",".").replace(".class",""))) {
                    File file = new File(classStr);
                    try {
                        byte[] bytes = fileToBytes(file);
                        ClassDefinition classDefinition = new ClassDefinition(c, bytes);
                        inst.redefineClasses(classDefinition);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("==========>>>>>>>>>>热更新完毕");

    }

    public static byte[] fileToBytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        in.close();
        return bytes;
    }


    public static void traverseFolder2(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                        classPathList.add(file2.getAbsolutePath());
                        System.out.println("=======>>>>>>获取到需要重载的class文件:   " + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }
}
