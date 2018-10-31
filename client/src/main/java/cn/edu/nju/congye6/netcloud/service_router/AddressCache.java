package cn.edu.nju.congye6.netcloud.service_router;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cong on 2018-10-31.
 */
public class AddressCache {

    private static final Map<String,List<String>> ADDRESS_CACHE=new ConcurrentHashMap<>();

    public List<String> getAddressList(String serviceName){
        return ADDRESS_CACHE.get(serviceName);
    }

    public void updateAddress(String serviceName,List<String> addressList){
        ADDRESS_CACHE.put(serviceName,addressList);
    }

    public void clearCache(){
        ADDRESS_CACHE.clear();
    }


}
