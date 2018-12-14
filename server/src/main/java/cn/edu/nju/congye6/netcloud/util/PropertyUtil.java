package cn.edu.nju.congye6.netcloud.util;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;

/**
 * 获取配置文件属性
 */
public class PropertyUtil {

    private static  final String PROPERTY_FILE_PATH="src/main/resources/application.properties";

    private static final Map<String,String> PROPERTY_MAP=new HashMap<>();

    private static boolean hasRead=false;

    public static String getProperty(String name){
        if(!hasRead){
            readProperty();
            hasRead=true;
        }
        return PROPERTY_MAP.get(name);
    }

    private static void readProperty(){
        File propertyFile=new File(PROPERTY_FILE_PATH);
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(propertyFile));
            while(true){
                String line=bufferedReader.readLine();
                if(line==null)
                    break;
                if(line.equals(""))
                    continue;
                String[] pair=line.split("=");
                if(pair.length!=2)
                    continue;
                PROPERTY_MAP.put(pair[0],pair[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        System.out.println(getProperty("cn.edu.nju.congye6.zookeeper.host"));
    }


}
