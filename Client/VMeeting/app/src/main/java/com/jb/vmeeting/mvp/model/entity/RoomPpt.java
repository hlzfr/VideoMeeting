package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 2016/5/19.
 */
public class RoomPpt extends BaseEntity {
    @Expose
    private String id;

    @Expose
    private String room;

    @Expose
    private List<String> ppts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<String> getPpts() {
        return ppts == null ? ppts = new ArrayList<>() : ppts;
    }

    public void setPpts(List<String> ppts) {
        this.ppts = ppts;
    }
}
