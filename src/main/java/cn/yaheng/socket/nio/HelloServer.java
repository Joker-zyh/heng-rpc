package cn.yaheng.socket.nio;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 86191
 * @description
 * @create 2024/8/12 17:44
 */
@Slf4j
public class HelloServer {

    public void start(int port){
        // 1. 创建serverSocket，并绑定一个接口
        try(
                ServerSocket serverSocket = new ServerSocket(port);
                ){
            // 2. 循环，接收客户端
            log.info("serverSocket start...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null){
                // 3. 获取输入流和输出流，读取信息
                log.info("client connected");
                try(
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        ){
                    // 4. 读取信息
                    Message message = (Message)objectInputStream.readObject();
                    log.info("HelloClient->HelloServer：{}", JSON.toJSONString(message));
                    // 5. 发送消息
                    message.setContent("hello client");
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HelloServer helloServer = new HelloServer();
        helloServer.start(6666);
    }
}
