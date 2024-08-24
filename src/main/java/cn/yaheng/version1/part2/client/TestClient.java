package cn.yaheng.version1.part2.client;

import cn.yaheng.version1.part2.client.proxy.ClientProxy;
import cn.yaheng.version1.part2.common.pojo.User;
import cn.yaheng.version1.part2.common.service.UserService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 86191
 * @description 测试客户端
 * @create 2024/8/22 22:15
 */
@Slf4j
public class TestClient {
    public static void main(String[] args) {
        // 1. 通过代理对象得到远程对象，调用其方法
        ClientProxy clientProxy = new ClientProxy("127.0.0.1",9999,1);
        UserService userService = clientProxy.getProxy(UserService.class);
        User user = userService.getUserById(111);
        log.info("user：{}", JSON.toJSONString(user));
    }
}
