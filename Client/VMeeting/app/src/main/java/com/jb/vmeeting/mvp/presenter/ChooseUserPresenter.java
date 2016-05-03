package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.app.constant.APIConstant;
import com.jb.vmeeting.mvp.model.apiservice.UserService;
import com.jb.vmeeting.mvp.model.entity.ChooseUserItem;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.mvp.view.IRefreshableListView;
import com.jb.vmeeting.tools.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class ChooseUserPresenter extends BaseRefreshablePresenter<ChooseUserItem>{

    UserService userService = RetrofitHelper.createService(UserService.class);

    ArrayList<User> notInclude;

    public ChooseUserPresenter(IRefreshableListView<ChooseUserItem> view) {
        super(view);
    }

    public void setNotInclude(ArrayList<User> notInclude) {
        this.notInclude = notInclude;
    }

    @Override
    public void refreshData() {
        L.d("refreshData notInclude " + (notInclude == null ? "null" : notInclude.toString()));
        userService.getUserPage(1, APIConstant.LIMIT_DEFINED_BY_SERVLET, notInclude == null ? new ArrayList<User>(0) : notInclude).enqueue(new SimpleCallback<Page<User>>() {
            @Override
            public void onSuccess(int statusCode, Result<Page<User>> result) {
                refreshSuccess(ChooseUserItem.toChooseUserItemPage(result.body));
            }

            @Override
            public void onFailed(int statusCode, Result<Page<User>> result) {
                refreshFailed(statusCode, result.code, result.message);
            }
        });
    }

    @Override
    public void loadMoreData(int page, int limit) {
        userService.getUserPage(page, limit, notInclude).enqueue(new SimpleCallback<Page<User>>() {
            @Override
            public void onSuccess(int statusCode, Result<Page<User>> result) {
                loadMoreSuccess(ChooseUserItem.toChooseUserItemPage(result.body));
            }

            @Override
            public void onFailed(int statusCode, Result<Page<User>> result) {
                loadMoreFailed(statusCode, result.code, result.message);
            }
        });
    }
}
