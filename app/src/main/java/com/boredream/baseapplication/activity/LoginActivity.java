package com.boredream.baseapplication.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.entity.LoginRequest;
import com.boredream.baseapplication.entity.User;
import com.boredream.baseapplication.net.ErrorConstants;
import com.boredream.baseapplication.net.GlideHelper;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.utils.DialogUtils;
import com.boredream.baseapplication.utils.TokenKeeper;
import com.boredream.baseapplication.utils.UserKeeper;
import com.boredream.baseapplication.view.EditTextWithClear;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.etwc_username)
    EditTextWithClear etUsername;
    @BindView(R.id.etwc_password)
    EditTextWithClear etPassword;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.btn_login)
    Button btnLogin;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initExtras();
        initView();
    }

    private void initExtras() {
    }

    private void initView() {
        GlideHelper.loadRoundedImg(ivLogo, R.mipmap.ic_launcher, 16);
        etPassword.getEt().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        if (!cbAgree.isChecked()) {
            showTip("请勾选统一用户协议和隐私政策");
            return;
        }

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (StringUtils.isEmpty(username)) {
            showTip("手机号不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            showTip("密码不能为空");
            return;
        }

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        HttpRequest.getInstance()
                .getApiService()
                .postUserLogin(request)
                .compose(RxComposer.commonProgress(this))
                .flatMap((Function<String, ObservableSource<User>>) token -> {
                    TokenKeeper.getSingleton().setToken(token);
                    return HttpRequest.getInstance()
                            .getApiService()
                            .getUserInfo()
                            .compose(RxComposer.commonProgress(LoginActivity.this));
                })
                .subscribe(new SimpleObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        loginSuccess(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ErrorConstants.compareApiError(e, "用户不存在")) {
                            register(request);
                        }
                    }
                });
    }

    private void register(LoginRequest request) {
        DialogUtils.show2BtnDialog(this, "注册", "您的账号是第一次登录，是否创建用户？", v ->
                HttpRequest.getInstance()
                        .getApiService()
                        .postUserRegister(request)
                        .compose(RxComposer.commonProgress(LoginActivity.this))
                        .flatMap((Function<String, ObservableSource<User>>) token -> {
                            TokenKeeper.getSingleton().setToken(token);
                            return HttpRequest.getInstance()
                                    .getApiService()
                                    .getUserInfo()
                                    .compose(RxComposer.commonProgress(LoginActivity.this));
                        })
                        .subscribe(new SimpleObserver<User>() {
                            @Override
                            public void onNext(User user) {
                                loginSuccess(user);
                            }
                        }));
    }

    private void loginSuccess(User user) {
        UserKeeper.getSingleton().setUser(user);
        finish();
        MainActivity.start(this);
    }

    @OnClick({R.id.tv_protocol, R.id.tv_privacy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_protocol:
                WebViewActivity.start(this, "http://www.papikoala.cn/lovebook/userprotocol.html", "用户协议");
                break;
            case R.id.tv_privacy:
                WebViewActivity.start(this, "http://www.papikoala.cn/lovebook/privacy.html", "隐私政策");
                break;
        }
    }
}
