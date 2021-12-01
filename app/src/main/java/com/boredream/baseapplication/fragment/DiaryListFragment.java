package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.activity.DiaryEditActivity;
import com.boredream.baseapplication.adapter.DiaryAdapter;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.entity.Diary;
import com.boredream.baseapplication.entity.dto.PageResultDTO;
import com.boredream.baseapplication.entity.event.DiaryUpdateEvent;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.view.TitleBar;
import com.boredream.baseapplication.view.decoration.LastPaddingItemDecoration;
import com.boredream.baseapplication.view.loading.RefreshListLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DiaryListFragment extends BaseFragment implements OnSelectedListener<Diary> {

    View view;
    Unbinder unbinder;

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rll)
    RefreshListLayout rll;

    private int curPage;
    private ArrayList<Diary> infoList = new ArrayList<>();
    private DiaryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_diary_list, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DiaryUpdateEvent event) {
        loadData(false);
    }

    private void initView() {
        titleBar.setTitle("日记")
                .setLeftMode()
                .setRight("列表模式", v -> commit());

        rll.setEnableRefresh(true);
        rll.setEnableLoadmore(false);
        rll.setOnRefreshListener(refresh -> loadData(false));
        rll.setOnLoadmoreListener(refresh -> loadData(true));
        rll.getRv().setLayoutManager(new LinearLayoutManager(activity));
        rll.getRv().setAdapter(adapter = new DiaryAdapter(infoList));
        rll.getRv().addItemDecoration(new LastPaddingItemDecoration());
        adapter.setOnItemClickListener(this);
    }

    private void commit() {
        
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
                .getDiaryPage(page, 20)
                .compose(RxComposer.commonRefresh(this, rll, loadMore))
                .subscribe(new SimpleObserver<PageResultDTO<Diary>>() {
                    @Override
                    public void onNext(PageResultDTO<Diary> response) {
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
    public void onSelected(Diary data) {
        DiaryEditActivity.start(activity, data);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        DiaryEditActivity.start(activity, null);
    }
}