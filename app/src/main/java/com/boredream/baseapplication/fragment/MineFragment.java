package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.activity.InviteCpActivity;
import com.boredream.baseapplication.activity.UserInfoActivity;
import com.boredream.baseapplication.adapter.SettingItemAdapter;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.entity.SettingItem;
import com.boredream.baseapplication.entity.User;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.net.GlideHelper;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.utils.DialogUtils;
import com.boredream.baseapplication.utils.UserKeeper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment implements OnSelectedListener<SettingItem> {

    private Unbinder unbinder;

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    private SettingItemAdapter adapter;
    private SettingItem cpSettingItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_mine, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        rvItems.setLayoutManager(new LinearLayoutManager(activity));
        ivAvatar.setOnClickListener(v -> UserInfoActivity.start(activity));
    }

    private void initData() {
        setSettingItems();
        setUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: chunyang 12/3/21 收到通知重新拉取
        setUserInfo();
    }

    private void setSettingItems() {
        cpSettingItem = new SettingItem(R.drawable.ic_setting_cp, "另一半", null, null, false);
        List<SettingItem> settingList = Arrays.asList(cpSettingItem,
                new SettingItem(R.drawable.ic_setting_love, "秀恩爱", null, null, false),
                new SettingItem(R.drawable.ic_setting_recommend, "推荐给大家", null, null, false),
                new SettingItem(R.drawable.ic_setting_about, "关于我们", null, null, false),
                new SettingItem(R.drawable.ic_setting_feed, "反馈", null, null, false)
        );
        adapter = new SettingItemAdapter(settingList);
        rvItems.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void setUserInfo() {
        User user = UserKeeper.getSingleton().getUser();
        GlideHelper.loadAvatar(ivAvatar, user);
        tvName.setText(user.getNickname());
        tvId.setText(String.format(Locale.getDefault(), "ID: %s", user.getShowId()));

        // cp
        String cpUserAvatar;
        if (user.getCpUser() != null) {
            cpUserAvatar = user.getCpUser().getAvatar();
            cpSettingItem.setRightText("解绑");
            cpSettingItem.setRightImage(cpUserAvatar);
        } else {
            cpSettingItem.setRightText("绑定");
            cpSettingItem.setRightImage(null);
        }
    }

    @Override
    public void onSelected(SettingItem data) {
        switch(data.getName()) {
            case "另一半":
                User user = UserKeeper.getSingleton().getUser();
                if (user.getCpUser() != null) {
                    // 解绑
                    unbindCp(user.getCpUser().getId());
                } else {
                    // 绑定
                    InviteCpActivity.start(activity);
                }
                break;
            case "秀恩爱":
                // TODO: chunyang 12/3/21  
                break;
            case "推荐给大家":
                break;
            case "关于我们":
                break;
            case "反馈":
                break;
        }
    }

    private void unbindCp(Long cpUserId) {
        HttpRequest.getInstance()
                .getApiService()
                .unbindUserCp(cpUserId)
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean response) {
                        // 解绑成功，重新获取个人信息
                        User user = UserKeeper.getSingleton().getUser();
                        user.setCpUserId(null);
                        user.setCpUser(null);
                        UserKeeper.getSingleton().setUser(user);

                        setUserInfo();
                    }
                });
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        DialogUtils.show2BtnDialog(activity, "提示", "是否确认退出登录？",
                (view) -> UserKeeper.getSingleton().logout(activity));
    }
}