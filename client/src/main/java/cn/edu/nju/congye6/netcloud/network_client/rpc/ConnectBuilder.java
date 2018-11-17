package cn.edu.nju.congye6.netcloud.network_client.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class ConnectBuilder {

    private static final Logger LOGGER = Logger.getLogger(ConnectBuilder.class);

    private static final String ADDRESS_SPLITER = ":";

    /**
     * 建立连接超时时间
     */
    private static final int CONNECT_TIMEOUT =10000;

    /**
     * 监听可用时间的多个selctor的线程池
     */
    private static final NioEventLoopGroup SELECTORS = new NioEventLoopGroup(4);

    /**
     * 创建连接
     *
     * @param address
     */
    public void build(String address,ChannelPool channelPool) {
        if (StringUtils.isEmpty(address)) {
            LOGGER.error("address empty");
            return;
        }

        String[] args = address.split(ADDRESS_SPLITER);
        if (args.length != 2) {
            LOGGER.error("address format wrong,address:" + address);
            return;
        }

        String host = args[0];
        Integer port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            LOGGER.error("wrong port:" + args[1], e);
            return;
        }
        build(host, port,channelPool);
    }

    public void build(String host, int port,ChannelPool channelPool) {
        Bootstrap b = new Bootstrap();
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT);//设置超时时间
        b.channel(NioSocketChannel.class)// 使用NioSocketChannel来作为连接用的channel类
                .group(SELECTORS)//设置selectors线程池
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcEncoder())//编码器，用于请求
                                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))//tcp粘包处理
                                .addLast(new RpcDecoder(RpcResponse.class))//解码器,每次获取到一个完整的响应才交给下一个handler
                                .addLast(new ResponseHandler());
                    }
                });
        LOGGER.info("created to " + host + ":" + port);
        ChannelFuture channelFuture=b.connect(host, port);// 异步连接服务器
        channelFuture.addListener(future -> {
            if(future.isCancelled()){
                //TODO 连接失败，重试
                return;
            }
            channelPool.addChannel(host+ADDRESS_SPLITER+port,channelFuture.channel());
        });
    }

}