package com.boredream.baseapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.fragment.BaseFragment;
import com.boredream.baseapplication.fragment.FragmentController;
import com.boredream.baseapplication.fragment.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private FragmentController controller;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());

        controller = new FragmentController(this, navigation, R.id.fl_content, fragments);
        controller.showFragment(0);
    }

}
