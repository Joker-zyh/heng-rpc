package cn.yaheng.version1.part3.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 86191
 * @description RPC调用返回对象
 * @create 2024/8/22 21:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RPCResponse implements Serializable {
    private int code;

    private String message;

    private Object data;

    public static RPCResponse success(Object data){
        return RPCResponse.builder()
                .code(200)
                .data(data)
                .build();
    }

    public static RPCResponse fail(){
        return RPCResponse.builder()
                .code(500)
                .message("服务器发送错误。")
                .build();
    }
}
