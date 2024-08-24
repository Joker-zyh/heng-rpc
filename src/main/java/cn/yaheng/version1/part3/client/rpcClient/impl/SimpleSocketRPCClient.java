package cn.yaheng.version1.part3.client.rpcClient.impl;

import cn.yaheng.version1.part3.client.rpcClient.RPCClient;
import cn.yaheng.version1.part3.common.message.RPCRequest;
import cn.yaheng.version1.part3.common.message.RPCResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author 86191
 * @description
 * @create 2024/8/24 17:32
 */
@Slf4j
@AllArgsConstructor
public class SimpleSocketRPCClient implements RPCClient {

    private final String host;
    private final Integer port;

    @Override
    public RPCResponse sendRequest(RPCRequest rpcRequest) {
        try {
            Socket socket = new Socket(host,port);
            log.info("通道建立成功。。。");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            log.info("写入请求对象：{}",rpcRequest);
            oos.writeObject(rpcRequest);
            oos.flush();


            // 3. 接收服务端返回的消息
            RPCResponse response = (RPCResponse) ois.readObject();
            log.info("接收到消息：{}",response);
            return response;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
