package com;

import nafos.core.Thread.Processors;
import nafos.core.util.*;
import net.sf.json.JSONArray;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * @Author 黄新宇
 * @Date 2018/10/11 上午11:21
 * @Description TODO
 **/
public class SocketTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(DateUtil.getNowTime());

//        StringBuffer content = new StringBuffer();
//        try {
////            File file = new File(RobotInit.class.getClassLoader().getResource("nameAndImg.json").getPath());
//            File file = new File("F:\\nameAndImg_back.json");
//            if (file.isFile() && file.exists()) {
//                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    content.append(lineTxt);
//                }
//            }
//        } catch (UnsupportedEncodingException | FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(ObjectUtil.isNull(content.toString())){
//            return;
//        }
//
//        JSONArray jsonArray = JSONArray.fromObject(content.toString());
//        for(int i =0;i<jsonArray.size();i++){
//           int shengdian = new Random().nextInt(900);
//           int hp = 0;
//           int role = 1;
//           int attack = 0;
//           if(shengdian<100){
//               hp = 2000+new Random().nextInt(5)*100;
//               role = 1;
//               attack = 20+new Random().nextInt(5)*20;
//           }
//
//            if(shengdian<300&&shengdian>=100){
//                role = new Random().nextInt(3)+1;
//                if(role == 1){
//                    hp = 2000 + new Random().nextInt(20)*100;
//                    attack = 20+new Random().nextInt(20)*20;
//                }
//                if(role == 2){
//                    hp = 4000 + new Random().nextInt(10)*100;
//                    attack = 20+new Random().nextInt(20)*20;
//                }
//                if(role == 3){
//                    hp = 3000 + new Random().nextInt(10)*100;
//                    attack = 40+new Random().nextInt(20)*20;
//                }
//            }
//
//            if(shengdian<500&&shengdian>=300){
//                role = new Random().nextInt(3)+1;
//                if(role == 1){
//                    hp = 2000 + (new Random().nextInt(50)+20)*100;
//                    attack = 20+(new Random().nextInt(50)+20)*20;
//                }
//                if(role == 2){
//                    hp = 4000 + (new Random().nextInt(30)+10)*100;
//                    attack = 20+(new Random().nextInt(30)+10)*20;
//                }
//                if(role == 3){
//                    hp = 3000 + (new Random().nextInt(20)+10)*100;
//                    attack = 40+(new Random().nextInt(20)+10)*20;
//                }
//            }
//
//            if(shengdian<900&&shengdian>=500){
//                role = new Random().nextInt(3)+1;
//                if(role == 1){
//                    hp = 2000 + (new Random().nextInt(80)+50)*100;
//                    attack = 20+(new Random().nextInt(80)+50)*20;
//                }
//                if(role == 2){
//                    hp = 4000 + (new Random().nextInt(80)+50)*100;
//                    attack = 20+(new Random().nextInt(80)+50)*20;
//                }
//                if(role == 3){
//                    hp = 3000 + (new Random().nextInt(80)+50)*100;
//                    attack = 40+(new Random().nextInt(80)+50)*20;
//                }
//            }
//
//            jsonArray.getJSONObject(i).put("victoryPiont",shengdian);
//           jsonArray.getJSONObject(i).put("hp",hp);
//            jsonArray.getJSONObject(i).put("attack",attack);
//            jsonArray.getJSONObject(i).put("baoji",new Random().nextInt(60));
//            jsonArray.getJSONObject(i).put("role",role);
//        }
//        System.out.println(jsonArray);


//        Socket socket = new Socket("127.0.0.1", 9988);
//
//        byte[] b = "{\"22\":1}".getBytes();
//        byte[] m = ArrayUtil.concat(ArrayUtil.concat(ArrayUtil.intToByteArray(1000),ArrayUtil.intToByteArray(1000)),b);
//        byte[] p = ArrayUtil.concat(ArrayUtil.intToByteArray(m.length),m);
//        System.out.println(p.length);
//        socket.getOutputStream().write(p);
//        socket.getOutputStream().flush();
//
//
//        Scanner scanner=new Scanner(System.in);
//        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        String readline;
//        while(true)                                                 //循环发消息
//        {
//            System.out.println(1);
//            readline=scanner.nextLine();
////            write.write(readline+'\n');                            //write()要加'\n'
////            write.flush();
////			socket.shutdownOutput();
//            System.out.println(in.readLine());
//        }
//
//
////        socket.close();

    }


}
