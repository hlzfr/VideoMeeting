package com.jb.vmeeting.network.helper;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jianbin on 2016/3/13.
 */
public class OKHttpHelper {

    private OkHttpClient mClient;

    public static OKHttpHelper getInstance() {
        return SingletonHolder.sOKHttpHelper;
    }

    public OKHttpHelper() {
        mClient = buildClient();
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    private OkHttpClient buildClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {

            // 该拦截对返回的权限Cookie信息进行解析和保存，并在每次发起请求时带上
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = chain.request().newBuilder();
                AuthCookie savedCookies = AuthCookie.getFromSp();
                if (savedCookies != null) {
                    List<String> cookieList = savedCookies.generateCookieList();
                    for (String cookieValue : cookieList) {
                        requestBuilder.addHeader("Cookie", cookieValue);
                    }
                    request = requestBuilder.build();
                }

                Response response = chain.proceed(request);
                List<String> cookies = response.headers().values("Set-Cookie");
                if (cookies.size() > 0) {
                    AuthCookie authCookie = AuthCookie.parseCookie(cookies);
                    boolean resetCookie = "true".equals(response.header("resetCookie", "false"));
                    boolean clearCookie = "true".equals(response.header("clearCookie","false"));
                    if (savedCookies != null && !resetCookie && !clearCookie) {
                        savedCookies.updateCookie(authCookie);
                    } else if(clearCookie){
                        savedCookies = null;
                    } else {
                        savedCookies = authCookie;
                    }
                    AuthCookie.save2Sp(savedCookies);
                }
                return response;
            }
        }).build();
        return client;
    }

    private static final class SingletonHolder {
        private static final OKHttpHelper sOKHttpHelper = new OKHttpHelper();
    }
}
