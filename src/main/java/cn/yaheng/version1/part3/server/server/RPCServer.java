package cn.yaheng.version1.part3.server.server;

/**
 * @author 86191
 * @description RPC服务端
 * @create 2024/8/22 22:25
 */
public interface RPCServer {
    void start(int port);

    void stop();
}
