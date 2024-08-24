package cn.yaheng.version1.part2.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 86191
 * @description RPC调用请求参数
 * @create 2024/8/22 21:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RPCRequest implements Serializable {
    // 接口名称
    private String interfaceName;
    // 方法名
    private String methodName;
    // 参数
    private Object[] params;
    // 参数类型
    private Class<?>[] paramsTypes;
}
