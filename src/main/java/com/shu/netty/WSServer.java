package com.shu.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/*服务器设置*/
@Component
public class WSServer {

    // 构建单例模式
    public static class SingletionWSServer {
        static final WSServer instance = new WSServer();
    }

    public static WSServer getInstance() {
        return SingletionWSServer.instance;
    }

    private EventLoopGroup mainGroup;// 主线程池
    private EventLoopGroup subGroup; // 工作线程池
    private ServerBootstrap server;// 服务器
    private ChannelFuture future; // 回调

    public WSServer() {
        mainGroup = new NioEventLoopGroup();//主线程池
        subGroup = new NioEventLoopGroup();//从线程池
        server = new ServerBootstrap();//创建Netty服务器启动对象
        server.group(mainGroup, subGroup)//netty服务器指定和配备主从线程池
                .channel(NioServerSocketChannel.class)//指定netty通道类型
                //指定通道初始化器用来加载当channel收到消息后
                //如何进行业务处理
                .childHandler(new WSServerInitialzer());
    }

    public void start(){
        this.future=server.bind(8088);
        System.err.println("netty websocket 启动");
    }



}
