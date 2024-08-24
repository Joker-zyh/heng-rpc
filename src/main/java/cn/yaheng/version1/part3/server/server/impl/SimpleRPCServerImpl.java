package cn.yaheng.version1.part3.server.server.impl;

import cn.yaheng.version1.part3.common.message.RPCRequest;
import cn.yaheng.version1.part3.common.message.RPCResponse;
import cn.yaheng.version1.part3.server.provider.ServiceProvider;
import cn.yaheng.version1.part3.server.server.RPCServer;
import cn.yaheng.version1.part3.server.server.work.WorkThread;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 86191
 * @description 简单RPC服务端实现类
 * @create 2024/8/22 22:26
 */
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SimpleRPCServerImpl implements RPCServer {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        // 1. 打开服务器Socket连接，监听端口号，监听连接消息
        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            log.info("服务器启动。。。");
            Socket socket;
            while ((socket = serverSocket.accept()) != null){
                log.info("链接到客户端。。。{}",socket);

                // 使用工作线程完成调用
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
           log.error(e.toString());
        }
    }

    @Override
    public void stop() {

    }

    private RPCResponse getResponse(RPCRequest rpcRequest){
        try{
            // 1. 通过反射调用服务方法
            Object service = serviceProvider.getService(rpcRequest.getInterfaceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsTypes());
            Object invoke = method.invoke(service, rpcRequest.getParams());

            // 2. 将返回值包装后返回
            return RPCResponse.success(invoke);

        }catch (Exception e){
            log.error("方法执行出错 ",e);
            return RPCResponse.fail();
        }
    }
}
