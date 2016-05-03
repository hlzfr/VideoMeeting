package com.jb.vmeeting.mvp.model.eventbus.event;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jianbin on 2016/4/27.
 */
public class RefreshEvent {
    public List<String> needRefresh;

    public RefreshEvent(String[] needRefresh) {
        this(Arrays.asList(needRefresh));
    }

    public RefreshEvent(List<String> needRefresh) {
        this.needRefresh = needRefresh;
    }
}
