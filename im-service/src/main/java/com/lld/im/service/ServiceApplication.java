package com.lld.im.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tangcj
 * @date 2023/05/24 22:08
 **/
@SpringBootApplication(scanBasePackages = {"com.lld.im.service", "com.lld.im.common"})
@MapperScan(basePackages = {"com.lld.im.service.**.mapper"})
//导入用户资料，删除用户资料，修改用户资料，查询用户资料
public class ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
