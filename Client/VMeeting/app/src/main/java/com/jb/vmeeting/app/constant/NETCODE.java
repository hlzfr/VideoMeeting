package com.jb.vmeeting.app.constant;

/**
 * Created by Jianbin on 2016/4/24.
 */
public class NETCODE {
    public static class RESULT {
        public static final int ERR_LOCAL = -1; // 请求服务器失败的本地错误
        public static final int CODE_RESULT_EMPTY = 0; // 服务端返回的result为空
        public static final int CODE_UNAUTH = 401; // 需要登录，没有权限

        public static final int CODE_NO_MORE = 1; // 没有更多
        public static final int CODE_LOGIN_NEED = 2; // 需要登录，收到这个消息需要自动跳转到login页面
    }

    public static class STATUS { // status code
        public static final int ERR_LOCAL = -1; // 请求服务器失败的本地错误
    }
}
