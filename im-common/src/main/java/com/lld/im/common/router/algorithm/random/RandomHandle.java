package com.lld.im.common.router.algorithm.random;

import com.lld.im.common.enums.UserErrorCode;
import com.lld.im.common.exception.ApplicationException;
import com.lld.im.common.router.RouteHandle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author tangcj
 * @date 2023/06/03 10:44
 **/
public class RandomHandle implements RouteHandle {

    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if (size == 0) {
            throw new ApplicationException(UserErrorCode.SERVER_NOT_AVAILABLE);
        }
        int i = ThreadLocalRandom.current().nextInt(size);
        return values.get(i);
    }
}
