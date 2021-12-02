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
import com.boredream.baseapplication.activity.UserInfoActivity;
import com.boredream.baseapplication.adapter.SettingItemAdapter;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.entity.SettingItem;
import com.boredream.baseapplication.entity.User;
import com.boredream.baseapplication.net.GlideHelper;
import com.boredream.baseapplication.utils.DialogUtils;
import com.boredream.baseapplication.utils.UserKeeper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_mine, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        loadData();
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

    private void loadData() {
        User user = UserKeeper.getSingleton().getUser();
        String cpUserAvatar = null;
        if (user.getCpUser() != null) {
            cpUserAvatar = user.getCpUser().getAvatar();
        }
        List<SettingItem> settingList = Arrays.asList(
                new SettingItem(R.drawable.ic_setting_cp, "另一半", "解绑", cpUserAvatar, false),
                new SettingItem(R.drawable.ic_setting_love, "秀恩爱", null, null, false),
                new SettingItem(R.drawable.ic_setting_recommend, "推荐给大家", null, null, false),
                new SettingItem(R.drawable.ic_setting_about, "关于我们", null, null, false),
                new SettingItem(R.drawable.ic_setting_feed, "反馈", null, null, false)
        );
        rvItems.setAdapter(new SettingItemAdapter(settingList));
        GlideHelper.loadOvalImg(ivAvatar, user.getAvatar());
        tvName.setText(user.getNickname());
        tvId.setText(String.format(Locale.getDefault(), "ID: %d", user.getId()));
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        DialogUtils.show2BtnDialog(activity, "提示", "是否确认退出登录？",
                (view) -> UserKeeper.getSingleton().logout(activity));
    }
}