package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.activity.DiaryEditActivity;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.entity.Diary;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.view.loading.RefreshListLayout;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DiaryCalendarFragment extends BaseFragment implements OnSelectedListener<Diary> {

    View view;
    Unbinder unbinder;

    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.rll_content)
    RefreshListLayout rllContent;
    @BindView(R.id.calendar_layout)
    CalendarLayout calendarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_diary_calendar, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
    }

    private void initData() {

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