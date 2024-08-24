package cn.yaheng.version1.part3.client.serviceCenter;

import java.net.InetSocketAddress;

/**
 * @author 86191
 * @description 服务中心
 * @create 2024/8/24 20:56
 */
public interface ServiceCenter {
    InetSocketAddress serviceDiscovery(String serviceName);
}
