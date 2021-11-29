package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TheDayFragment extends BaseFragment {

    View view;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_the_day, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        loadData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        rv.setLayoutManager(new GridLayoutManager(activity, 2));
//        adapter = new TvAdapter(activity, tvList);
//        rv.setAdapter(adapter);

        refresh.setEnableRefresh(true);
        refresh.setEnableLoadmore(false);
        refresh.setOnRefreshListener(refresh -> {
            loadData();
        });
    }

    private void loadData() {
    }
}