package com.jb.vmeeting.mvp.model.eventbus.event;

import com.jb.vmeeting.mvp.model.entity.User;

import java.util.List;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class AddParticipatorEvent {
    public List<User> participator;

    public AddParticipatorEvent(List<User> participator) {
        this.participator = participator;
    }
}
