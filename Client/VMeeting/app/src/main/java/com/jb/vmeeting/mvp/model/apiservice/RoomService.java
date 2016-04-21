package com.jb.vmeeting.mvp.model.apiservice;

import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.Room;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 房间相关api接口
 * Created by Jianbin on 2016/4/21.
 */
public interface RoomService {

    public static final int LIMIT_DEFINED_BY_SERVLET = -1;

    /**
     *
     * @param skip
     * @param limit -1 means it's defined by servlet.
     * @return
     */
    @GET("room/roomList")
    Call<Page<Room>> getRoomList(@Query(value = "skip")int skip, @Query(value = "limit") int limit);

}
