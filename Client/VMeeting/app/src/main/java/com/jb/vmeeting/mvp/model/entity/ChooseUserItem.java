package com.jb.vmeeting.mvp.model.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class ChooseUserItem {
    private boolean isChoose = false;
    private User user;

    public ChooseUserItem(boolean choose, User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public static Page<ChooseUserItem> toChooseUserItemPage(@NonNull Page<User> userPage) {
        List<ChooseUserItem> chooseUserItems = new ArrayList<>(userPage.getData().size());
        for (User user : userPage.getData()) {
            chooseUserItems.add(new ChooseUserItem(false, user));
        }
        Page<ChooseUserItem> chooseUserItemPage = new Page<>();
        chooseUserItemPage.setData(chooseUserItems);
        chooseUserItemPage.setPageInfo(userPage.getPageInfo());
        return chooseUserItemPage;
    }

    @Override
    public String toString() {
        return "isChoose=" + isChoose + ", " + (user == null ? "null" : user.toString());
    }
}
