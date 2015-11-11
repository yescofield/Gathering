package com.yezimm.gathering.htpp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by yezimm on 2015/11/11.
 */
public class SocketThread extends Thread{

    Socket socket = null;

    public SocketThread() {
        try {
            this.socket = new Socket("10.141.54.22", 20155);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            System.out.println("客户端开始连接");
            //一直读取控制台
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                //包体
                byte [] content = br.readLine().getBytes();
                //包头,固定4个字节,包含包体长度信息
                byte [] head = Tool.intToByteArray1(content.length);
                BufferedOutputStream bis = new BufferedOutputStream(socket.getOutputStream());
                bis.write(head);
                bis.flush();
                bis.write(content);
                bis.flush();
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static class Tool {
        //int 转字节数组
        public static byte[] intToByteArray1(int i) {
            byte[] result = new byte[4];
            result[0] = (byte)((i >> 24) & 0xFF);
            result[1] = (byte)((i >> 16) & 0xFF);
            result[2] = (byte)((i >> 8) & 0xFF);
            result[3] = (byte)(i & 0xFF);
            return result;
        }

        public static byte[] intToByteArray2(int i) throws Exception {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(buf);
            out.writeInt(i);
            byte[] b = buf.toByteArray();
            out.close();
            buf.close();
            return b;
        }

        //字节数组转int
        public static int byteArrayToInt(byte[] b) {
            int intValue=0;
            for(int i=0;i<b.length;i++){
                intValue +=(b[i] & 0xFF)<<(8*(3-i));
            }
            return intValue;
        }
    }

}
