package com.jb.vmeeting.page.fragment;

import android.os.Bundle;
import android.view.View;

import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.ChooseUserItem;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.AddParticipatorEvent;
import com.jb.vmeeting.mvp.presenter.ChooseUserPresenter;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.page.adapter.recyclerview.ArrayAdapter;
import com.jb.vmeeting.page.adapter.recyclerview.UserListAdapter;
import com.jb.vmeeting.tools.L;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class ChooseParticipatorFragment extends SimpleListFragment<ChooseUserItem> {

//    ArrayList<User> notInclude;

    public static ChooseParticipatorFragment newInstance(ArrayList<User> notInclude) {
        ChooseParticipatorFragment fragment = new ChooseParticipatorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.BUNDLE_KEY_USER_LIST, notInclude);
        L.d("bundle.putSerializable(IntentConstant.BUNDLE_KEY_USER_LIST, notInclude); "+(notInclude==null?"null":notInclude.toString()));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BaseRefreshablePresenter<ChooseUserItem> createPresenter(View contentView) {
        ChooseUserPresenter chooseUserPresenter = new ChooseUserPresenter(this);
        Bundle arguments = getArguments();
        ArrayList<User> userList = (ArrayList<User>) arguments.getSerializable(IntentConstant.BUNDLE_KEY_USER_LIST);
        if (userList != null && userList.size() > 0) {
//            notInclude = userList;
            L.d("chooseUserPresenter.setNotInclude(userList) "+userList.toString());
            chooseUserPresenter.setNotInclude(userList);
        }
        return chooseUserPresenter;
    }

    @Override
    public ArrayAdapter<ChooseUserItem> createAdapter(View contentView) {
        return new UserListAdapter(getActivity());
    }

    @Override
    public void onItemClick(View view, int position) {
        ChooseUserItem item = getAdapter().getItem(position);
        item.setIsChoose(!item.isChoose());
        getAdapter().notifyItemChanged(position);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        return false;
    }

    @Override
    public void onDestroy() {
//        postCompleteEvent();
        super.onDestroy();
    }

    public void postCompleteEvent() {
        List<ChooseUserItem> list = getAdapter().getData();
        if (list != null && list.size() > 0) {
            List<User> participator = new ArrayList<>(list.size());
            for (ChooseUserItem item : list) {
                if (item.isChoose()) {
                    participator.add(item.getUser());
                }
            }
            L.d("EventBus.getDefault().post(new AddParticipatorEvent(participator));");
            EventBus.getDefault().post(new AddParticipatorEvent(participator));
        }
    }
}
