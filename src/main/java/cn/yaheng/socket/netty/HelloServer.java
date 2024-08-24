package cn.yaheng.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 86191
 * @description Hello服务端
 * @create 2024/8/14 15:25
 */
@Slf4j
public class HelloServer {
    public static void main(String[] args) {
        // 1. 初始化启动器，添加组件，并启动
        log.info("HelloServer 开启....");
        new ServerBootstrap()
                // 2. 事件组
                .group(new NioEventLoopGroup())
                // 3. 通道
                .channel(NioServerSocketChannel.class)
                // 4. 执行器,负责添加别的执行器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        // 5. 解码
                        socketChannel.pipeline().addLast(new StringDecoder());
                        // 6. 自定义执行器
                        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            // 7. 读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("HelloServer收到：{}",msg);
                            }
                        });
                    }
                })
                .bind(6666);

    }
}
