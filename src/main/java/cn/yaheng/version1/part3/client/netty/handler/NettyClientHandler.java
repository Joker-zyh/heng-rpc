package cn.yaheng.version1.part3.client.netty.handler;

import cn.yaheng.version1.part3.common.message.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 86191
 * @description Netty业务客户端拦截器，只读取RPCResponse
 * @create 2024/8/24 16:59
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse) throws Exception {
        // 1. 将Channel写别名，并将相应结果写入
        AttributeKey<RPCResponse> attributeKey = AttributeKey.valueOf("RPCResponse");
        channelHandlerContext.channel().attr(attributeKey).set(rpcResponse);
        channelHandlerContext.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Netty客户端业务拦截器异常",cause);
        ctx.close();
    }
}
