package cn.yaheng.version1.part2.client.rpcClient.impl;

import cn.yaheng.version1.part2.client.netty.nettyInitializer.NettyClientInitializer;
import cn.yaheng.version1.part2.client.rpcClient.RPCClient;
import cn.yaheng.version1.part2.common.message.RPCRequest;
import cn.yaheng.version1.part2.common.message.RPCResponse;
import com.alibaba.fastjson2.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 86191
 * @description NettyRPC客户端
 * @create 2024/8/24 16:48
 */
@Slf4j
public class NettyRPCClient implements RPCClient {
    private static final Bootstrap bootstrap;

    private final String host;
    private final Integer port;

    public NettyRPCClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    static {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    @Override
    public RPCResponse sendRequest(RPCRequest rpcRequest) {
        try{
            // 1. 连接服务器
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            log.info("连接服务器成功。。。");
            // 2. 获取Channel，并写入数据
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcRequest);
            // 3. 阻塞获取结果
            channel.closeFuture().sync();

            // 4. 通过别名获取响应结果
            AttributeKey<RPCResponse> attributeKey = AttributeKey.valueOf("RPCResponse");
            RPCResponse rpcResponse = channel.attr(attributeKey).get();
            log.info("获取到响应结果：{}", JSON.toJSONString(rpcResponse));
            return rpcResponse;

        }catch (InterruptedException e){
            log.error("发送RPC请求失败",e);
        }
        return null;
    }
}
