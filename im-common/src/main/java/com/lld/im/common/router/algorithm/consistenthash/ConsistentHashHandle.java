package com.lld.im.common.router.algorithm.consistenthash;

import com.lld.im.common.router.RouteHandle;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/06/03 12:19
 **/
public class ConsistentHashHandle implements RouteHandle {

    private AbstractConsistentHash hash;

    public void setHash(AbstractConsistentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values, key);
    }
}
