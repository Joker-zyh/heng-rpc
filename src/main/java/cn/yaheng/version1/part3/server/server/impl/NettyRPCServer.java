package cn.yaheng.version1.part3.server.server.impl;

import cn.yaheng.version1.part3.server.netty.nettyInitializer.NettyServerInitializer;
import cn.yaheng.version1.part3.server.provider.ServiceProvider;
import cn.yaheng.version1.part3.server.server.RPCServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 86191
 * @description NettyRPC服务器
 * @create 2024/8/24 17:14
 */
@Slf4j
@AllArgsConstructor
public class NettyRPCServer implements RPCServer {
    private final ServiceProvider serviceProvider;


    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(serviceProvider));
            log.info("Netty服务器启动。。。");
            // 同步阻塞绑定
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 死循环监听
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            log.error("Netty服务器出错",e);
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    @Override
    public void stop() {

    }
}
