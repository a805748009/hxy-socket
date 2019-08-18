package nafos;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author 黄新宇
 * @Date 2018/5/19 下午6:33
 * @Description package下所有java类生成proto文件，供前端使用
 **/
public class BeanToProtoFile {


    public static void run(String packPath, String filePath) {
        List<Class> classes = getClasssFromPackage(packPath);
        for (Class clas : classes) {
            castProto(clas, filePath);
        }
    }


    public static void castProto(Class clazz, String filePath) {
        try {
            File file = new File(filePath + clazz.getSimpleName() + ".proto");
            PrintStream ps = new PrintStream(new FileOutputStream(file));


            List<Field> fieldList = new ArrayList<>();
            Class tempClass = clazz;
//        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
//            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
//        }
            ps.println("message " + tempClass.getSimpleName() + "{");
            int i = 1;
            for (Field f : fieldList) {
                if (f.getType().getName().equals("java.lang.Integer")) {
                    ps.append("    optional int32 " + f.getName() + " = " + i + ";" + "\n");
                }
                if (f.getType().getName().equals("java.lang.Boolean")) {
                    ps.append("    optional bool " + f.getName() + " = " + i + ";" + "\n");
                }
                if (f.getType().getName().equals("java.lang.String")) {
                    ps.append("    optional string " + f.getName() + " = " + i + ";" + "\n");
                }
                if (f.getType().getName().equals("int")) {
                    ps.append("    optional int32 " + f.getName() + " = " + i + ";" + "\n");
                }
                if (f.getType().getName().equals("java.util.List")) {
                    ps.append("    repeated int32 " + f.getName() + " = " + i + ";" + "\n");
                }
                if (f.getType().getName().equals("long")) {
                    ps.append("    optional int64 " + f.getName() + " = " + i + ";" + "n");
                }
                i++;
            }
            ps.append("}");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static List<Class> getClasssFromPackage(String pack) {
        List<Class> clazzs = new ArrayList<Class>();

        // 是否循环搜索子包
        boolean recursive = true;

        // 包名字
        String packageName = pack;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    System.out.println("file类型的扫描");
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
                } else if ("jar".equals(protocol)) {
                    System.out.println("jar类型的扫描");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzs;
    }

    /**
     * 在package对应的路径下找到所有的class
     *
     * @param packageName package名称
     * @param filePath    package对应的路径
     * @param recursive   是否查找子package
     * @param clazzs      找到class以后存放的集合
     */
    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
                return acceptDir || acceptClass;
            }
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
