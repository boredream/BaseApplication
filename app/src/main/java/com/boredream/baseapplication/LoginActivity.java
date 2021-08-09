package com.boredream.baseapplication;

import android.os.Bundle;

import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.databinding.ActivityLoginBinding;
import com.boredream.baseapplication.ui.LoginViewModel;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }

}