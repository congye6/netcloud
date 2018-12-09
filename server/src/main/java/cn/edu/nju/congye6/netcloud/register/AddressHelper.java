package cn.edu.nju.congye6.netcloud.register;

import cn.edu.nju.congye6.netcloud.util.IpAddressUtil;
import cn.edu.nju.congye6.netcloud.util.PropertyUtil;
import org.springframework.util.StringUtils;

/**
 * Created by cong on 2018-11-05.
 */
public class AddressHelper {

    private static final String RPC_PORT_KEY = "cn.edu.nju.congye6.rpc.port";

    private static final String SERVER_PORT_KEY = "server.port";

    private static final String SERVER_ADDRESS_KEY = "server.address";

    private static final String DEFAULT_ADDRESS = "127.0.0.1";

    private static final String DEFAULT_PORT = "8080";

    private static final String SPLITER=":";

    /**
     * 获取ip：port地址
     * @return
     */
    String getAddress(){
        String ip=getServerAddress();
        String port=getServerPort();
        return ip+SPLITER+port;
    }

    String getRpcAddress(){
        String ip=getServerAddress();
        String port=PropertyUtil.getProperty(RPC_PORT_KEY);
        return ip+SPLITER+port;
    }

    private String getServerAddress() {
        String address = PropertyUtil.getProperty(SERVER_ADDRESS_KEY);
        if (!StringUtils.isEmpty(address))
            return address;
        address = IpAddressUtil.getLocalHostLANAddress();
        if (!StringUtils.isEmpty(address))
            return address;
        return DEFAULT_ADDRESS;
    }

    private String getServerPort() {
        String port = PropertyUtil.getProperty(SERVER_PORT_KEY);
        if (!StringUtils.isEmpty(port))
            return port;
        return DEFAULT_PORT;
    }

}
