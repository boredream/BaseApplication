package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.collection.ArraySet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.activity.TheDayEditActivity;
import com.boredream.baseapplication.adapter.TheDayAdapter;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.entity.TheDay;
import com.boredream.baseapplication.entity.dto.PageResultDTO;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.view.decoration.LastPaddingItemDecoration;
import com.boredream.baseapplication.view.loading.RefreshListLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TheDayFragment extends BaseFragment implements OnSelectedListener<TheDay> {

    View view;
    Unbinder unbinder;

    @BindView(R.id.iv_right_add)
    ImageView ivRightAdd;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_left)
    RelativeLayout rlLeft;
    @BindView(R.id.rll)
    RefreshListLayout rll;

    private int curPage;
    private ArrayList<TheDay> infoList = new ArrayList<>();
    private TheDayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_the_day, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        rll.setEnableRefresh(true);
        rll.setEnableLoadmore(false);
        rll.setOnRefreshListener(refresh -> loadData(false));
        rll.setOnLoadmoreListener(refresh -> loadData(true));
        rll.getRv().setLayoutManager(new LinearLayoutManager(activity));
        rll.getRv().setAdapter(adapter = new TheDayAdapter(infoList));
        rll.getRv().addItemDecoration(new LastPaddingItemDecoration());
        adapter.setOnItemClickListener(this);
    }

    private void initData() {
        loadData(false);
    }

    private void loadData(boolean loadMore) {
        int page;
        if (!loadMore) {
            page = curPage = 1;
        } else {
            page = curPage + 1;
        }

        HttpRequest.getInstance()
                .getApiService()
                .getTheDayPage(null, page, 20)
                .compose(RxComposer.commonRefresh(this, rll, loadMore))
                .subscribe(new SimpleObserver<PageResultDTO<TheDay>>() {
                    @Override
                    public void onNext(PageResultDTO<TheDay> response) {
                        curPage = page;
                        if (curPage == 1) {
                            infoList.clear();
                        }

                        infoList.addAll(response.getRecords());
                        rll.setEnableLoadmore(response.getCurrent() < response.getPages());
                        rll.checkEmpty();
                    }
                });
    }

    @Override
    public void onSelected(TheDay data) {
        TheDayEditActivity.start(activity, data);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        TheDayEditActivity.start(activity, null);
    }
}