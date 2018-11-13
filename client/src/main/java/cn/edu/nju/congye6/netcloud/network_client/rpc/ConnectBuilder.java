package cn.edu.nju.congye6.netcloud.network_client.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
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
     * 监听可用时间的多个selctor的线程池
     */
    private static final NioEventLoopGroup SELECTORS = new NioEventLoopGroup(4);

    /**
     * 创建连接
     *
     * @param address
     */
    public Channel build(String address) {
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
        return build(host, port);
    }

    public Channel build(String host, int port) {

        Bootstrap b = new Bootstrap();
        b.channel(NioSocketChannel.class)// 使用NioSocketChannel来作为连接用的channel类
                .group(SELECTORS)
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //TODO 顺序
                        ch.pipeline().addLast(new RpcEncoder())
                                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                .addLast(new RpcDecoder(RpcResponse.class))
                                .addLast(new EchoClientHandler());
                    }
                });
        LOGGER.info("created to " + host + ":" + port);

        try {
            ChannelFuture cf = b.connect(host, port).sync(); // 异步连接服务器
            return cf.channel();
        } catch (InterruptedException e) {
            LOGGER.error("connect error", e);
        }
        LOGGER.info("connected..."); // 连接完成
        return null;
    }

}