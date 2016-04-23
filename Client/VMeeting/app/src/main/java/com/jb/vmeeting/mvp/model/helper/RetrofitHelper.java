package com.jb.vmeeting.mvp.model.helper;

import com.google.gson.GsonBuilder;
import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;

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
                    .addConverterFactory(
                            GsonConverterFactory.create(
                                    new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 没有@Expose注释的属性将不会被序列化
                                            .create()))
                    .baseUrl(App.getInstance().getString(R.string.common_base_url))
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
