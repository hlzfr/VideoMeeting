package com.jb.vmeeting.mvp.model.eventbus.event;

import com.jb.vmeeting.mvp.model.entity.User;

import java.util.List;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class RemoveParticipatorEvent {
    public List<User> participator;

    public RemoveParticipatorEvent(List<User> participator) {
        this.participator = participator;
    }
}
