package com.jb.vmeeting.mvp.model.biz;

import com.jb.vmeeting.mvp.model.entity.User;

/**
 * 弃用Biz模块，因为觉得无意义地增加了复杂度
 * Created by Jianbin on 16/3/14.
 */
@Deprecated
public interface ISignUpBiz {
    void signUp(User user);
}
