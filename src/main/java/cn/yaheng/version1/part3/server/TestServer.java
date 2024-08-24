package cn.yaheng.version1.part3.server;

import cn.yaheng.version1.part3.common.service.UserService;
import cn.yaheng.version1.part3.common.service.impl.UserServiceImpl;
import cn.yaheng.version1.part3.server.provider.ServiceProvider;
import cn.yaheng.version1.part3.server.server.RPCServer;
import cn.yaheng.version1.part3.server.server.impl.NettyRPCServer;

/**
 * @author 86191
 * @description 测试服务端
 * @create 2024/8/22 22:54
 */
public class TestServer {
    public static void main(String[] args) {
        // 1. 初始化服务容器
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",9999);
        UserService userService = new UserServiceImpl();
        serviceProvider.saveServices(userService);

        //RPCServer server = new SimpleRPCServerImpl(serviceProvider);
        RPCServer server = new NettyRPCServer(serviceProvider);
        server.start(9999);
    }
}
