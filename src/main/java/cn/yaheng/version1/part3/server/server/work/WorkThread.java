package cn.yaheng.version1.part3.server.server.work;

import cn.yaheng.version1.part3.common.message.RPCRequest;
import cn.yaheng.version1.part3.common.message.RPCResponse;
import cn.yaheng.version1.part3.server.provider.ServiceProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author 86191
 * @description 工作线程
 * @create 2024/8/22 22:32
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class WorkThread implements Runnable {
    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        // 1. 通过输入流获取请求对象
        try  {
            log.info("获取到通道：{}",socket);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            log.info("获取到请求对象：{}",rpcRequest);
            // 2. 获取到返回对象
            RPCResponse response = getResponse(rpcRequest);
            // 3. 返回给客户端
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
