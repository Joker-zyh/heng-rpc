package cn.yaheng.version1.part2.client.proxy;

import cn.yaheng.version1.part2.client.rpcClient.RPCClient;
import cn.yaheng.version1.part2.client.rpcClient.impl.NettyRPCClient;
import cn.yaheng.version1.part2.client.rpcClient.impl.SimpleSocketRPCClient;
import cn.yaheng.version1.part2.common.message.RPCRequest;
import cn.yaheng.version1.part2.common.message.RPCResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 86191
 * @description 客户端代理对象
 * @create 2024/8/22 22:00
 */
@Slf4j
public class ClientProxy implements InvocationHandler {

    private RPCClient rpcClient;

    public ClientProxy(String host, int port, int rpcClientChose) {
        switch (rpcClientChose) {
            case 0:
                this.rpcClient = new SimpleSocketRPCClient(host, port);
                break;
            case 1:
                this.rpcClient = new NettyRPCClient(host, port);
                break;
        }
    }

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
        RPCResponse rpcResponse = rpcClient.sendRequest(rpcRequest);
        return rpcResponse.getData();
    }

    /**
     * 获取代理对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

}
