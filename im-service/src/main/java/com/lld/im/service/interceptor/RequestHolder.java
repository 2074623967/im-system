package com.lld.im.service.interceptor;

/**
 * @author tangcj
 * @date 2023/06/03 22:32
 **/
public class RequestHolder {

    private final static ThreadLocal<Boolean> requestHolder = new ThreadLocal<>();

    public static void set(Boolean isadmin) {
        requestHolder.set(isadmin);
    }

    public static Boolean get() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }
}
