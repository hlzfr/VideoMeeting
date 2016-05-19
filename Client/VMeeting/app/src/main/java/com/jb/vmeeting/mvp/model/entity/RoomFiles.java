package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 2016/5/18.
 */
public class RoomFiles extends BaseEntity {
    @Expose
    private String id;

    @Expose
    private String room;

    @Expose
    private List<String> files;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<String> getFiles() {
        return files == null ? files = new ArrayList<>() : files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
