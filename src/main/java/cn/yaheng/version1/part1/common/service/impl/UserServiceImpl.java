package cn.yaheng.version1.part1.common.service.impl;

import cn.yaheng.version1.part1.common.pojo.User;
import cn.yaheng.version1.part1.common.service.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * @author 86191
 * @description 用户Service实现类
 * @create 2024/8/22 21:50
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(int userId) {
        Random random = new Random();
        return User.builder()
                .id(userId)
                .name(UUID.randomUUID().toString())
                .sex(random.nextBoolean())
                .build();
    }
}
