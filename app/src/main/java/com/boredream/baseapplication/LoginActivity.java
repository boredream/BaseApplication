package com.boredream.baseapplication;

import android.os.Bundle;

import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.databinding.ActivityLoginBinding;
import com.boredream.baseapplication.ui.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> {

    @BindView(R.id.et_username)
    TextInputEditText etUsername;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;

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

    @OnClick(R.id.btn_send_code)
    public void onClick() {
        viewModel.sendCode(etUsername.getText() + "");
    }
}