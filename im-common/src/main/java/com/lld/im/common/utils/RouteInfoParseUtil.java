package com.lld.im.common.utils;

import com.lld.im.common.BaseErrorCode;
import com.lld.im.common.exception.ApplicationException;
import com.lld.im.common.router.RouteInfo;

/**
 * @author tangcj
 * @date 2023/06/03 11:13
 **/
public class RouteInfoParseUtil {

    public static RouteInfo parse(String info) {
        try {
            String[] serverInfo = info.split(":");
            RouteInfo routeInfo = new RouteInfo(serverInfo[0], Integer.parseInt(serverInfo[1]));
            return routeInfo;
        } catch (Exception e) {
            throw new ApplicationException(BaseErrorCode.PARAMETER_ERROR);
        }
    }
}
