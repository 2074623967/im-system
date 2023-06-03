package com.lld.im.service.config;

import com.lld.im.common.config.AppConfig;
import com.lld.im.common.enums.ImUrlRouteWayEnum;
import com.lld.im.common.enums.RouteHashMethodEnum;
import com.lld.im.common.router.RouteHandle;
import com.lld.im.common.router.algorithm.consistenthash.AbstractConsistentHash;
import com.lld.im.common.router.algorithm.consistenthash.ConsistentHashHandle;
import com.lld.im.common.router.algorithm.consistenthash.TreeMapConsistentHash;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Method;

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
        Integer imRouteWay = appConfig.getImRouteWay();
        String routWay = "";
        ImUrlRouteWayEnum handler = ImUrlRouteWayEnum.getHandler(imRouteWay);
        routWay = handler.getClazz();
        RouteHandle routeHandle = (RouteHandle) Class.forName(routWay).newInstance();
        if (handler == ImUrlRouteWayEnum.HASH) {
            Method setHash = Class.forName(routWay).getMethod("setHash", AbstractConsistentHash.class);
            Integer consistentHashWay = appConfig.getConsistentHashWay();
            String hashWay;
            RouteHashMethodEnum hashHandler = RouteHashMethodEnum.getHandler(consistentHashWay);
            hashWay = hashHandler.getClazz();
            AbstractConsistentHash consistentHash = (AbstractConsistentHash) Class.forName(hashWay).newInstance();
            setHash.invoke(routeHandle, consistentHash);
        }
        return routeHandle;
    }
}
