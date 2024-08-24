package cn.yaheng.version1.part3.client.rpcClient.impl;

import cn.yaheng.version1.part3.client.netty.nettyInitializer.NettyClientInitializer;
import cn.yaheng.version1.part3.client.rpcClient.RPCClient;
import cn.yaheng.version1.part3.client.serviceCenter.ServiceCenter;
import cn.yaheng.version1.part3.client.serviceCenter.ZKServiceCenter;
import cn.yaheng.version1.part3.common.message.RPCRequest;
import cn.yaheng.version1.part3.common.message.RPCResponse;
import com.alibaba.fastjson2.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author 86191
 * @description NettyRPC客户端
 * @create 2024/8/24 16:48
 */
@Slf4j
public class NettyRPCClient implements RPCClient {
    private static final Bootstrap bootstrap;

    private final ServiceCenter serviceCenter;

    public NettyRPCClient() {
        this.serviceCenter = new ZKServiceCenter();
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
            InetSocketAddress inetSocketAddress = serviceCenter.serviceDiscovery(rpcRequest.getInterfaceName());
            ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress.getHostName(), inetSocketAddress.getPort()).sync();
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
