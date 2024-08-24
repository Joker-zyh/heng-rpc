package cn.yaheng.version1.part2.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 86191
 * @description 用户类
 * @create 2024/8/22 21:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    // id
    private Integer id;

    // 用户名
    private String name;

    // 性别 - 0：男 ； 1：女
    private Boolean sex;
}
