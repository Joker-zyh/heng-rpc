package cn.yaheng.version1.part3.server.provider;

import cn.yaheng.version1.part3.server.serviceRegister.ServiceRegister;
import cn.yaheng.version1.part3.server.serviceRegister.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 86191
 * @description 服务提供类
 * @create 2024/8/22 22:18
 */
public class ServiceProvider {
    private final Map<String, Object> services = new HashMap<>();

    private final ServiceRegister serviceRegister;
    private final String host;
    private final Integer port;

    public ServiceProvider(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.serviceRegister = new ZKServiceRegister();
    }

    /**
     * 保存该接口下的所有实现类
     * @param service
     */
    public void saveServices(Object service){
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class<?> clazz : interfaces){
            // 保存到本地
            services.put(clazz.getName(),service);
            // 保存到zookeeper
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port));
        }

    }

    public Object getService(String serviceName){
        return services.get(serviceName);
    }
}
