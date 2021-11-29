package com.boredream.baseapplication.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.entity.LoginRequest;
import com.boredream.baseapplication.entity.User;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.utils.TokenKeeper;
import com.boredream.baseapplication.utils.UserKeeper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private User user;

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

        // FIXME: 2019-08-26
        etUsername.setText("18501683421");
        etPassword.setText("123456");
    }

    private void initExtras() {
    }

    private void initView() {
        ivClose.setOnClickListener(v -> finish());
        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (StringUtils.isEmpty(username)) {
            showTip("用户名不能为空");
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
                .flatMap(new Function<String, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(String token) throws Exception {
                        TokenKeeper.getSingleton().setToken(token);
                        return  HttpRequest.getInstance()
                                .getApiService()
                                .getUserInfo()
                                .compose(RxComposer.commonProgress(LoginActivity.this));
                    }
                })
                .subscribe(new SimpleObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        loginSuccess(user);
                    }
                });
    }

    private void loginSuccess(User user) {
        UserKeeper.getSingleton().setUser(user);
        finish();
        MainActivity.start(this);
    }

}
