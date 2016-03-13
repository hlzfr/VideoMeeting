package com.jb.vmeeting.network.helper;

import com.jb.vmeeting.tools.storage.SimpleSharedPreference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 2016/3/6.
 */
public class AuthCookie implements Serializable{

    private String rememberMeCode = "";
    private String rememberMeMethod = "";

    private String jSessionId = "";

    public String getjSessionId() {
        return jSessionId;
    }

    public void setjSessionId(String jSessionId) {
        this.jSessionId = jSessionId;
    }

    public String getRememberMeCode() {
        return rememberMeCode;
    }

    public void setRememberMeCode(String rememberMeCode) {
        this.rememberMeCode = rememberMeCode;
    }

    public String getRememberMeMethod() {
        return rememberMeMethod;
    }

    public void setRememberMeMethod(String rememberMeMethod) {
        this.rememberMeMethod = rememberMeMethod;
    }

    public List<String> generateCookieList() {
        List<String> values = new ArrayList<>(3);
        if (!isEmpty(jSessionId)) values.add(jSessionId);
        if (!isEmpty(rememberMeCode)) values.add(rememberMeCode);
        if (!isEmpty(rememberMeMethod)) values.add(rememberMeMethod);
        return values;
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static void clear() {
        SimpleSharedPreference sp = SimpleSharedPreference.getInstance();
        sp.put("jSessionId", "");
        sp.put("rememberMeMethod", "");
        sp.put("rememberMeCode", "");
    }

    public static void save2Sp(AuthCookie authCookie) {
        if (authCookie == null) {
            authCookie = new AuthCookie();
        }
        SimpleSharedPreference sp = SimpleSharedPreference.getInstance();
        sp.put("jSessionId", authCookie.jSessionId);
        sp.put("rememberMeMethod", authCookie.rememberMeMethod);
        sp.put("rememberMeCode", authCookie.rememberMeCode);
    }

    public static AuthCookie getFromSp() {
        SimpleSharedPreference sp = SimpleSharedPreference.getInstance();
        String jSessionId = sp.getString("jSessionId", "");
        String rememberMeMethod = sp.getString("rememberMeMethod", "");
        String rememberMeCode = sp.getString("rememberMeCode", "");
        AuthCookie authCookie = new AuthCookie();
        authCookie.setRememberMeMethod(rememberMeMethod);
        authCookie.setjSessionId(jSessionId);
        authCookie.setRememberMeCode(rememberMeCode);
        return authCookie;
    }

    public static AuthCookie parseCookie(List<String> headers) {
        AuthCookie authCookie = new AuthCookie();
        for (String value : headers) {
            if (value.startsWith("JSESSIONID")) {
                authCookie.setjSessionId(value);
            } else if (value.startsWith("rememberMe=deleteMe")) {
                authCookie.setRememberMeMethod(value);
            } else if (value.startsWith("rememberMe")) {
                authCookie.setRememberMeCode(value);
            }
        }
        return authCookie;
    }

    public void updateCookie(AuthCookie cookie) {
        if (cookie == null) return;
        if (!isEmpty(cookie.getjSessionId())) {
            this.setjSessionId(cookie.getjSessionId());
        }
        if (!isEmpty(cookie.getRememberMeCode())) {
            this.setRememberMeCode(cookie.getRememberMeCode());
        }
        if (!isEmpty(cookie.getRememberMeMethod())) {
            this.setRememberMeMethod(cookie.getRememberMeMethod());
        }
    }
}
