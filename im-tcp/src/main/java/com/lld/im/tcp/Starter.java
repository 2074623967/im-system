package com.lld.im.tcp;

import com.lld.im.codec.config.BootstrapConfig;
import com.lld.im.tcp.server.LimServer;
import com.lld.im.tcp.server.LimWebSocketServer;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 启动类
 *
 * @author tangcj
 * @date 2023/05/30 14:15
 **/
public class Starter {

    //    HTTP GET POST PUT DELETE 1.0 1.1 2.0
    //client IOS 安卓 pc(windows mac) web //支持json 也支持 protobuf
    //appId
    //28 + imei + body
    //请求头(指令 版本 clientType 消息解析类型 imei长度 appId bodylen) + imei号 + 请求体
    //len+body

    public static void main(String[] args) {
        String path = "D:\\ideadaima\\mukewang\\即时通讯\\im-system\\im-tcp\\src\\main\\resources\\config.yml";
        start(path);
    }

    private static void start(String path) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(path);
            BootstrapConfig bootstrapConfig = yaml.loadAs(inputStream, BootstrapConfig.class);
            new LimServer(bootstrapConfig.getLim()).start();
            new LimWebSocketServer(bootstrapConfig.getLim()).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(500);
        }
    }

}
