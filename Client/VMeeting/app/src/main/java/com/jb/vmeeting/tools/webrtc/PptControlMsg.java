package com.jb.vmeeting.tools.webrtc;

import com.google.gson.annotations.Expose;

/**
 * Created by Jianbin on 2016/5/19.
 */
public class PptControlMsg {
    public static final int TYPE_MOVE = 0;
    public static final int TYPE_DELETE = 1;
    public static final int TYPE_ADD = 2;
//    public static final int TYPE_ADD = 1;

    @Expose
    public int type = TYPE_ADD;
    @Expose
    public int position = 0;
    @Expose
    public String url = "";
}
