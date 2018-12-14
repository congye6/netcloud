package cn.edu.nju.congye6.netcloud.network_client.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ConnectBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectBuilder.class);

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
     * 异步创建连接
     *
     * @param address
     */
    public void build(String address,ChannelPool channelPool) {
        ChannelFuture channelFuture = buildChannel(address);
        if(channelFuture==null)
            return;
        channelFuture.addListener(future -> {
            if(future.isCancelled()){
                //TODO 连接失败，重试
                return;
            }
            channelPool.addChannel(address,channelFuture.channel());
        });
    }


    /**
     * 同步获取channel
     * @return
     */
    public Channel buildSync(String address) {
        ChannelFuture channelFuture = buildChannel(address);
        if(channelFuture==null)
            return null;
        try {
            return channelFuture.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ChannelFuture buildChannel(String address) {
        if (StringUtils.isEmpty(address)) {
            LOGGER.error("address empty");
            return null;
        }
        String[] args = address.split(ADDRESS_SPLITER);
        if (args.length != 2) {
            LOGGER.error("address format wrong,address:" + address);
            return null;
        }
        String host = args[0];
        Integer port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            LOGGER.error("wrong port:" + args[1], e);
            return null;
        }
        Bootstrap b = new Bootstrap();
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT);//设置超时时间
        b.channel(NioSocketChannel.class)// 使用NioSocketChannel来作为连接用的channel类
                .group(SELECTORS)//设置selectors线程池
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcEncoder())//编码器，用于请求
                                .addLast(new RpcDecoder(RpcResponse.class))//解码器,每次获取到一个完整的响应才交给下一个handler
                                .addLast(new ResponseHandler());
                    }
                });
        LOGGER.info("created channel to " + host + ":" + port);
        return b.connect(host, port);
    }

}