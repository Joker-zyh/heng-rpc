package cn.yaheng.socket.nio;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author 86191
 * @description
 * @create 2024/8/12 18:10
 */
@Slf4j
public class HelloClient {

    public Object send(Message message, String host, int port){
        // 1. 创建Socket，连接服务器
        try (
                Socket socket = new Socket(host,port);
                ){
            // 2. 获取输出流，发送消息
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);

            // 3. 获取输入流，得到服务器传递的信息
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HelloClient helloClient = new HelloClient();
        Message message = new Message("hello server");
        Message responseMessage = (Message) helloClient.send(message, "127.0.0.1", 6666);
        log.info("HelloServer->HelloClient:{}", JSON.toJSONString(message));
    }
}
