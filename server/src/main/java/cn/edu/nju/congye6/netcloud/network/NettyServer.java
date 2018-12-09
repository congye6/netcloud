package cn.edu.nju.congye6.netcloud.network;


import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyServer {

    /**
     * 长度字段占多少字节
     */
    private static final int LENGTH_FILED_LENGTH=4;

    private static final String RPC_PORT_KEY = "cn.edu.nju.congye6.rpc.port";

    public void startServerInPort() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //设置启动辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)//设置channel类型
            .childOption(ChannelOption.SO_KEEPALIVE,true)
            .childHandler(new ChannelHandlerInitializer());//选择执行handler
            
            //阻塞等待服务器完全启动
            int port=Integer.parseInt(PropertyUtil.getProperty(RPC_PORT_KEY));
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            
            channelFuture.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 添加handler
     */
    private class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //LengthFieldBasedFrameDecoder的输出会保留长度字段
            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,LENGTH_FILED_LENGTH));
            ch.pipeline().addLast(new RequestHandler());
            ch.pipeline().addLast(new ResponseEncoder());
        }
    }
}