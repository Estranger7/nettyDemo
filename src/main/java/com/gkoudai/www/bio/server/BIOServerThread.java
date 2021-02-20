package com.gkoudai.www.bio.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by：Estranger
 * Description：TODO
 * Date：2020/4/25 14:20
 */
public class BIOServerThread {

   public static ExecutorService newFixedThreadPool(int nThreads){
      return new ThreadPoolExecutor(
              nThreads,
              nThreads,
              0,
              TimeUnit.SECONDS,
              new LinkedBlockingDeque<Runnable>());
   }

   public static void main(String[] args) throws IOException {
      // TODO 服务端处理客户端连接请求
      ServerSocket serverSocket = new ServerSocket(3333);
      //创建固定线程数的线程池，用于接收客户端的请求，分配线程去处理
      ExecutorService executorService = newFixedThreadPool(3);
         executorService.execute(() -> {
            try {
               Socket socket = serverSocket.accept();
               int len;
               byte[] data = new byte[1024];
               InputStream inputStream = socket.getInputStream();
               // 按字节流方式读取数据
               while ((len = inputStream.read(data)) != -1) {
                  System.out.println(new String(data, 0, len));
               }
            } catch (IOException e) {
               e.printStackTrace();
            }
         });
      }
}
