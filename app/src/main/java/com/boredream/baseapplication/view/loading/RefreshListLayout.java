package com.boredream.baseapplication.view.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.boredream.baseapplication.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefreshListLayout extends SmartRefreshLayout implements ILoadingView {

    @BindView(R.id.refresh_list_rv)
    RecyclerView rv;
    @BindView(R.id.refresh_list_iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.refresh_list_tv_empty)
    TextView tvEmpty;
    @BindView(R.id.refresh_list_ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.refresh_list_ll_loading)
    LinearLayout llLoading;

    public RecyclerView getRv() {
        return rv;
    }

    public ImageView getIvEmpty() {
        return ivEmpty;
    }

    public TextView getTvEmpty() {
        return tvEmpty;
    }

    public LinearLayout getLlEmpty() {
        return llEmpty;
    }

    public LinearLayout getLlLoading() {
        return llLoading;
    }

    public RefreshListLayout(Context context) {
        super(context);
        init();
    }

    public RefreshListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_list_layout, this);
        ButterKnife.bind(this);
    }

    public void checkEmpty() {
        if (rv.getAdapter() == null) return;
        rv.getAdapter().notifyDataSetChanged();
        if (rv.getAdapter().getItemCount() == 0) {
            llEmpty.setVisibility(VISIBLE);
        } else {
            llEmpty.setVisibility(GONE);
        }
    }

    public void showRefresh(boolean loadMore) {
        // 加载更多时，一般都不是手动触发，无视
        if (loadMore) return;

        // 如果正在refreshing，不用处理
        if (isRefreshing()) return;

        // 显示布局loading
        llLoading.setVisibility(VISIBLE);
    }

    public void dismissRefresh(boolean loadMore) {
        // 不同loading状态，不同dismiss处理
        if (loadMore) {
            finishLoadmore();
        } else {
            if (isRefreshing()) {
                finishRefresh();
            }
            if (llLoading.getVisibility() == VISIBLE) {
                llLoading.setVisibility(GONE);
            }
        }
    }

    @Override
    public void doOnSubscribe(boolean loadMore) {
        showRefresh(loadMore);
    }

    public void doOnError(Throwable throwable, boolean loadMore) {
        dismissRefresh(loadMore);
    }

    public <T> void doOnNext(T t, boolean loadMore) {
        dismissRefresh(loadMore);
    }
}
