package com.lld.im.service.utils;

import com.lld.im.common.constant.Constants;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangcj
 * @date 2023/05/31 14:33
 **/
@Component
public class ZKit {

    private static Logger logger = LoggerFactory.getLogger(ZKit.class);

    @Resource
    private ZkClient zkClient;

    /**
     * get all TCP server node from zookeeper
     *
     * @return java.util.List<java.lang.String>
     * @date 2023/06/03 11:08
     **/
    public List<String> getAllTcpNode() {
        List<String> children = zkClient.getChildren(Constants.ImCoreZkRoot + Constants.ImCoreZkRootTcp);
//        logger.info("Query all node =[{}] success.", JSON.toJSONString(children));
        return children;
    }

    /**
     * get all WEB server node from zookeeper
     *
     * @return java.util.List<java.lang.String>
     * @date 2023/06/03 11:08
     **/
    public List<String> getAllWebNode() {
        List<String> children = zkClient.getChildren(Constants.ImCoreZkRoot + Constants.ImCoreZkRootWeb);
//        logger.info("Query all node =[{}] success.", JSON.toJSONString(children));
        return children;
    }
}
