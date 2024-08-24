package cn.yaheng.version1.part3.server.netty.handler;

import cn.yaheng.version1.part3.common.message.RPCResponse;
import cn.yaheng.version1.part3.server.provider.ServiceProvider;
import cn.yaheng.version1.part3.common.message.RPCRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author 86191
 * @description
 * @create 2024/8/24 17:21
 */
@AllArgsConstructor
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private final ServiceProvider serviceProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {
        // 反射调用方法，获取响应结果
        RPCResponse response = getResponse(rpcRequest);
        // 写回响应结果
        channelHandlerContext.writeAndFlush(response);
        channelHandlerContext.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Netty服务器业务拦截器异常",cause);
        ctx.close();
    }

    private RPCResponse getResponse(RPCRequest rpcRequest){
        try{
            // 1. 通过反射调用服务方法
            Object service = serviceProvider.getService(rpcRequest.getInterfaceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsTypes());
            Object invoke = method.invoke(service, rpcRequest.getParams());

            // 2. 将返回值包装后返回
            return RPCResponse.success(invoke);

        }catch (Exception e){
            log.error("方法执行出错 ",e);
            return RPCResponse.fail();
        }
    }
}
