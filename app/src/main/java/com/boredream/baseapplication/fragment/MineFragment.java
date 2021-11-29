package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.adapter.SettingItemAdapter;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.entity.SettingItem;
import com.boredream.baseapplication.entity.User;
import com.boredream.baseapplication.net.GlideHelper;
import com.boredream.baseapplication.utils.UserKeeper;
import com.boredream.baseapplication.view.decoration.LeftPaddingItemDecoration;

import java.util.Arrays;
import java.util.List;

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
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
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
        List<SettingItem> settingList = Arrays.asList(
                new SettingItem(R.drawable.ic_notifications_black_24dp, "另一半", "解绑", false),
                new SettingItem(R.drawable.ic_notifications_black_24dp, "秀恩爱", null, false),
                new SettingItem(R.drawable.ic_notifications_black_24dp, "推荐给大家", null, false),
                new SettingItem(R.drawable.ic_notifications_black_24dp, "关于我们", null, false),
                new SettingItem(R.drawable.ic_notifications_black_24dp, "反馈", null, false)
        );
        rvItems.setLayoutManager(new LinearLayoutManager(activity));
        rvItems.addItemDecoration(new LeftPaddingItemDecoration(activity));
        rvItems.setAdapter(new SettingItemAdapter(settingList));

    }

    private void loadData() {
        User user = UserKeeper.getSingleton().getUser();
        GlideHelper.loadOvalImg(ivAvatar, user.getAvatar());
        tvName.setText(user.getNickname());
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {

    }
}