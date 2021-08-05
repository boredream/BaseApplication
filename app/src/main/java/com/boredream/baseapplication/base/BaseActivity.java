package com.boredream.baseapplication.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

public class BaseActivity extends RxAppCompatActivity implements BaseView {

    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: chunyang 2/23/21 自定义新建进度框
//        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public <T> LifecycleTransformer<T> getLifeCycleTransformer() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    public Activity getViewContext() {
        return this;
    }

    @Override
    public void showTip(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (progressDialog == null || progressDialog.isShowing()) return;
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        if (progressDialog == null || !progressDialog.isShowing()) return;
        progressDialog.dismiss();
    }
}
