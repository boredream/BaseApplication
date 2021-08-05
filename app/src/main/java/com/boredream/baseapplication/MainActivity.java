package com.boredream.baseapplication;

import android.os.Bundle;

import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.entity.BeanInfo;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleDisObserver;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void commit() {
        // compose = 组成
        // 已经在里面封装了：自定义处理线程、接口返回时如果页面销毁不会处理、自定显示关闭进度框、自动处理返回数据、自动处理异常进行toast显示等逻辑
        // 这里只要关注正常返回即可，或者有特殊需求可自定义处理error，或自行组装compose

        HttpRequest.getInstance()
                .getApiService()
                .getInfo("123")
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleDisObserver<BeanInfo>() {
                    @Override
                    public void onNext(BeanInfo coopVisitR) {

                    }
                });


        // 无需进度显示的
        HttpRequest.getInstance()
                .getApiService()
                .getInfo("345")
                .compose(RxComposer.schedulers())
                .compose(RxComposer.lifecycle(this))
                .compose(RxComposer.defaultResponse())
                .compose(RxComposer.defaultFailed(this))
                // .compose(handleProgress(view)); 不处理进度
                .subscribe(new SimpleDisObserver<BeanInfo>() {
                    @Override
                    public void onNext(BeanInfo coopVisitR) {
                        // 显示数据处理
                    }

                    @Override
                    public void onError(Throwable e) {
                        showTip("错误了");
                    }
                });

    }
}