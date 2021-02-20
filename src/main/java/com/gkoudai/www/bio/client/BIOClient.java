package com.gkoudai.www.bio.client;


import java.io.*;
import java.net.*;
import java.util.Date;

/**
 * Created by：Estranger
 * Description：TODO
 * Date：2020/4/25 14:10
 */
public class BIOClient {
    public static void main(String[] args) {
        // TODO 创建多个线程，模拟多个客户端连接服务端
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    Socket socket = new Socket("127.0.0.1", 3333);
                    while (true) {
                        try {
                            socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                            Thread.sleep(2000);
                        } catch (Exception e) {
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
