package com.boredream.baseapplication.base;


import android.app.Application;
import android.content.Context;

import com.boredream.baseapplication.utils.AppKeeper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppKeeper.init(this);
        initRefresh();
    }

    private void initRefresh() {
        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) ->
                new ClassicsHeader(context));

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) ->
                new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate));
    }
}
