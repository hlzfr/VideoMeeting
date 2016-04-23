package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jianbin on 2016/4/21.
 */
public class Room extends BaseEntity {

    @Expose
    private String id;

    @Expose
    @SerializedName("roomName")
    private String name;

    @Expose
    @SerializedName("ownerName")
    private String ownerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
