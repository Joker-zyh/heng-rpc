package cn.yaheng.version1.part3.server.serviceRegister;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;

/**
 * @author 86191
 * @description 服务注册中心
 * @create 2024/8/24 21:10
 */
@Slf4j
public class ZKServiceRegister implements ServiceRegister{
    // 客户端
    private final CuratorFramework client;
    // zookeeper根路径
    private static final String ROOT_PATH = "MyRPC";

    // 客户端初始化，连接zookeeper服务器
    public ZKServiceRegister() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1")
                .sessionTimeoutMs(40000)
                .retryPolicy(retryPolicy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        log.info(" zookeeper 连接成功。。。");
    }

    @Override
    public void register(String serviceName, InetSocketAddress serviceAddress) {
        try {
            // 创建永久节点，服务提供者下线时，不删除服务名，只删除地址
            if (client.checkExists().forPath("/" + serviceName) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            // 路径地址
            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            // 临时节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        }catch (Exception e){
            log.error("服务注册失败",e);
        }
    }

    private String getServiceAddress(InetSocketAddress serviceAddress){
        return serviceAddress.getHostName() + ":" + serviceAddress.getPort();
    }

    private InetSocketAddress parseAddress(String address){
        String[] split = address.split(":");
        return new InetSocketAddress(split[0],Integer.parseInt(split[1]));
    }
}
