package com.jb.vmeeting.network.helper;

import com.jb.vmeeting.app.constant.URLConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Jianbin on 2016/2/17.
 */
public class RetrofitHelper {
    static class RetrofitHolder{
        public static final Retrofit INSTANCE = buildInstance();
        public static Retrofit buildInstance () {
            return  new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLConstant.BASE_URL)
                    .client(OKHttpHelper.getInstance().getClient())
                    .build();
        }
    }

    public static Retrofit getRetrofit() {
        return RetrofitHolder.INSTANCE;
    }

    public static <T> T createService(final Class<T> service) {
        return getRetrofit().create(service);
    }
}
