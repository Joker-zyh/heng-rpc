package cn.yaheng.version1.part2.client.rpcClient;

import cn.yaheng.version1.part2.common.message.RPCRequest;
import cn.yaheng.version1.part2.common.message.RPCResponse;

/**
 * @author 86191
 * @description RPC客户端接口
 * @create 2024/8/24 16:46
 */
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest rpcRequest);
}
