package cn.yaheng.version1.part3.client.serviceCenter;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author 86191
 * @description 服务中心
 * @create 2024/8/24 20:57
 */
@Slf4j
public class ZKServiceCenter implements ServiceCenter{
    // 客户端
    private final CuratorFramework client;
    // zookeeper根路径
    private static final String ROOT_PATH = "MyRPC";

    // 客户端初始化，连接zookeeper服务器
    public ZKServiceCenter() {
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
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> strings = client.getChildren().forPath("/" + serviceName);
            String address = strings.get(0);
            return parseAddress(address);
        }catch (Exception e){
            log.error("服务发现失败",e);
        }
        return null;
    }

    private String getServiceAddress(InetSocketAddress serviceAddress){
        return serviceAddress.getHostName() + ":" + serviceAddress.getPort();
    }

    private InetSocketAddress parseAddress(String address){
        String[] split = address.split(":");
        return new InetSocketAddress(split[0],Integer.parseInt(split[1]));
    }


}
