package com.boredream.baseapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.adapter.SettingItemAdapter;
import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.entity.SettingItem;
import com.boredream.baseapplication.entity.User;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.utils.UserKeeper;
import com.boredream.baseapplication.view.TitleBar;
import com.boredream.baseapplication.view.decoration.LeftPaddingItemDecoration;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rv_items)
    RecyclerView rvItems;

    private User user;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        initView();
        loadData();
    }

    private void initView() {
        user = UserKeeper.getSingleton().getUser();
        titleBar.setTitle("个人资料").setLeftBack().setRight("完成", v -> commit());
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.addItemDecoration(new LeftPaddingItemDecoration(this));
    }

    private void commit() {
        HttpRequest.getInstance()
                .getApiService()
                .putUser(user.getId(), user)
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        showTip("修改成功");
                        finish();
                    }
                });
    }

    private void loadData() {
        String cpUserAvatar = null;
        if (user.getCpUser() != null) {
            cpUserAvatar = user.getCpUser().getAvatar();
        }
        List<SettingItem> settingList = Arrays.asList(
                new SettingItem(null, "头像", null, cpUserAvatar, true),
                new SettingItem(null, "昵称", user.getNickname(), null, true),
                new SettingItem(null, "性别", user.getGender(), null, true),
                new SettingItem(null, "生日", user.getBirthday(), null, true)
        );
        SettingItemAdapter adapter = new SettingItemAdapter(settingList);
        rvItems.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }
    }
}