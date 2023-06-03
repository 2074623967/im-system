package com.lld.im.service.config;

import com.lld.im.common.config.AppConfig;
import com.lld.im.common.router.RouteHandle;
import com.lld.im.common.router.algorithm.consistenthash.ConsistentHashHandle;
import com.lld.im.common.router.algorithm.consistenthash.TreeMapConsistentHash;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author tangcj
 * @date 2023/06/03 10:48
 **/
@Configuration
public class BeanConfig {

    @Resource
    private AppConfig appConfig;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(appConfig.getZkAddr(),
                appConfig.getZkConnectTimeOut());
    }

    @Bean
    public RouteHandle routeHandle() throws Exception {
        ConsistentHashHandle consistentHandle = new ConsistentHashHandle();
        TreeMapConsistentHash treeMapConsistentHash = new TreeMapConsistentHash();
        consistentHandle.setHash(treeMapConsistentHash);
        return consistentHandle;
    }
}
