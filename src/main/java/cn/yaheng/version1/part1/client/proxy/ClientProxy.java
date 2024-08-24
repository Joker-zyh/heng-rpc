package cn.yaheng.version1.part1.client.proxy;

import cn.yaheng.version1.part1.common.message.RPCRequest;
import cn.yaheng.version1.part1.common.message.RPCResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author 86191
 * @description 客户端代理对象
 * @create 2024/8/22 22:00
 */
@Slf4j
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    private String host;
    private int port;

    /**
     * 增强的代码
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 生成RPCRequest对象
        RPCRequest rpcRequest = RPCRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes())
                .build();
        // 2. 发送请求对象到服务端
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
            return response.getData();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取代理对象
     * @param clazz
     * @return
     * @param <T>
     */
    public <T>T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
    }

}
