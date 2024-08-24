package cn.yaheng.version1.part2.server.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 86191
 * @description 服务提供类
 * @create 2024/8/22 22:18
 */
public class ServiceProvider {
    private final Map<String, Object> services = new HashMap<>();

    /**
     * 保存该接口下的所有实现类
     * @param service
     */
    public void saveServices(Object service){
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class<?> clazz : interfaces){
            services.put(clazz.getName(),service);
        }

    }

    public Object getService(String serviceName){
        return services.get(serviceName);
    }
}
