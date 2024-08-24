package cn.yaheng.version1.part3.server.serviceRegister;

import java.net.InetSocketAddress;

/**
 * @author 86191
 * @description 服务注册中心接口
 * @create 2024/8/24 21:09
 */
public interface ServiceRegister {
    void register(String serviceName, InetSocketAddress serviceAddress);
}
