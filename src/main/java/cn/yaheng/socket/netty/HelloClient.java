package cn.yaheng.socket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 86191
 * @description Hello客户端
 * @create 2024/8/14 15:35
 */
@Slf4j
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        // 1. 启动器
        log.info("HelloClient 开启并发送消息。。。");
        new Bootstrap()
                // 2. selector事件组，和服务器保持一致格式
                .group(new NioEventLoopGroup())
                // 3. 管道
                .channel(NioSocketChannel.class)
                // 4. 执行器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        // 5. 编码
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("127.0.0.1",6666)
                .sync()
                .channel()
                .writeAndFlush("Hello World");
    }
}
