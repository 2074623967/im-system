package com.lld.im.common.router.algorithm.loop;

import com.lld.im.common.enums.UserErrorCode;
import com.lld.im.common.exception.ApplicationException;
import com.lld.im.common.router.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author tangcj
 * @date 2023/06/03 10:45
 **/
public class LoopHandle implements RouteHandle {

    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if(size == 0){
            throw new ApplicationException(UserErrorCode.SERVER_NOT_AVAILABLE);
        }
        Long l = index.incrementAndGet() % size;
        if(l < 0){
            l = 0L;
        }
        return values.get(l.intValue());
    }
}
