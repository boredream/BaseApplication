package com.boredream.baseapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.LogUtils;
import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.databinding.ActivityLoginBinding;
import com.boredream.baseapplication.ui.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> {

    @BindView(R.id.btn_send_code)
    Button btnSendCode;

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