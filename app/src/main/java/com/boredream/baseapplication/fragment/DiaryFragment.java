package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.LogUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.activity.DiaryEditActivity;
import com.boredream.baseapplication.adapter.DiaryAdapter;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.entity.Diary;
import com.boredream.baseapplication.entity.dto.PageResultDTO;
import com.boredream.baseapplication.entity.event.DiaryUpdateEvent;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.utils.DateUtils;
import com.boredream.baseapplication.view.TitleBar;
import com.boredream.baseapplication.view.decoration.LastPaddingItemDecoration;
import com.boredream.baseapplication.view.loading.RefreshListLayout;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarUtil;
import com.haibin.calendarview.CalendarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DiaryFragment extends BaseFragment {

    View view;
    Unbinder unbinder;

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rll_list)
    RefreshListLayout rllList;
    @BindView(R.id.calendar_layout)
    CalendarLayout calendarLayout;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.rll_calendar)
    RefreshListLayout rllCalendar;

    private boolean showCalendar;

    private int listCurPage;
    private ArrayList<Diary> listInfoList = new ArrayList<>();

    private Calendar selectedDay;
    private HashMap<String, ArrayList<Diary>> allMonthCalendarInfoList = new HashMap<>();
    private ArrayList<Diary> calendarInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_diary, null);
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
                .setRight("列表模式", v -> changeShowCalendar(!showCalendar));

        selectedDay = Calendar.getInstance();
        initRefreshListLayout(rllList, listInfoList);
        initRefreshListLayout(rllCalendar, calendarInfoList);
        rllCalendar.setEnableRefresh(false);
        calendarView.setOnMonthChangeListener((year, month) -> {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.YEAR, year);
//            calendar.set(Calendar.MONTH, month);
//            curYearMonth = DateUtils.calendar2str(calendar, "yyyy-MM");
//            loadCalendarData();
        });
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(com.haibin.calendarview.Calendar calendar, boolean isClick) {
                selectedDay.setTimeInMillis(calendar.getTimeInMillis());
                loadCalendarData();
            }
        });
    }

    private void initRefreshListLayout(RefreshListLayout rll, ArrayList<Diary> infoList) {
        rll.setEnableRefresh(true);
        rll.setEnableLoadmore(false);
        rll.setOnRefreshListener(refresh -> loadData(false));
        rll.setOnLoadmoreListener(refresh -> loadData(true));
        rll.getRv().setLayoutManager(new LinearLayoutManager(activity));
        rll.getRv().setAdapter(new DiaryAdapter(infoList));
        rll.getRv().addItemDecoration(new LastPaddingItemDecoration());
    }

    private void changeShowCalendar(boolean showCalendar) {
        this.showCalendar = showCalendar;
        titleBar.getTvRight().setText(showCalendar ? "列表模式" : "日历模式");
        calendarLayout.setVisibility(showCalendar ? View.VISIBLE : View.GONE);
        rllList.setVisibility(showCalendar ? View.GONE : View.VISIBLE);

        // 切换样式的时候清空，并重新拉取数据
//        listInfoList.clear();
//        rllList.checkEmpty();
//        allMonthCalendarInfoList.clear();
//        calendarInfoList.clear();
//        rllCalendar.checkEmpty();
        loadData(false);
    }

    private void initData() {
        loadData(false);
    }

    private void loadData(boolean loadMore) {
        if (showCalendar) {
            loadCalendarData();
        } else {
            loadListData(loadMore);
        }
    }

    private void loadCalendarData() {
        // 先从缓存取当月数据
        String curYearMonth = DateUtils.calendar2str(selectedDay, "yyyy-MM");
        boolean success = loadDayDateFromLocal(curYearMonth);

        if (!success) {
            // 无数据，从服务器拉取
            loadMonthDataFromRemote(curYearMonth);
        }
    }

    private boolean loadDayDateFromLocal(String curYearMonth) {
        // 先从缓存取当月数据
        ArrayList<Diary> list = allMonthCalendarInfoList.get(curYearMonth);
        if (list == null) {
            return false;
        }

        // 有数据，匹配到当天
        calendarInfoList.clear();
        for (Diary diary : list) {
            if (DateUtils.isSameDay(selectedDay, DateUtils.str2calendar(diary.getDiaryDate()))) {
                calendarInfoList.add(diary);
            }
        }
        rllCalendar.checkEmpty();
        return true;
    }

    private void loadMonthDataFromRemote(String curYearMonth) {
        HttpRequest.getInstance()
                .getApiService()
                .getDiaryMonth(curYearMonth, 1, 999)
                .compose(RxComposer.commonRefresh(this, rllCalendar, false))
                .subscribe(new SimpleObserver<PageResultDTO<Diary>>() {
                    @Override
                    public void onNext(PageResultDTO<Diary> response) {
                        // 获取成功后，存入月信息
                        allMonthCalendarInfoList.put(curYearMonth, new ArrayList<>(response.getRecords()));

                        // 更新日历红点提示
                        updateCalendarHint();

                        // 刷新列表，再次从本地获取显示
                        loadDayDateFromLocal(curYearMonth);
                    }
                });
    }

    private void updateCalendarHint() {
        for (Map.Entry<String, ArrayList<Diary>> entry : allMonthCalendarInfoList.entrySet()) {
            ArrayList<Diary> list = entry.getValue();
            if(list != null) {
                for (Diary diary : list) {
                    Calendar calendar = DateUtils.str2calendar(diary.getDiaryDate());
                    com.haibin.calendarview.Calendar schema = new com.haibin.calendarview.Calendar();
                    schema.setYear(calendar.get(Calendar.YEAR));
                    schema.setMonth(calendar.get(Calendar.MONTH) + 1);
                    schema.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                    calendarView.addSchemeDate(schema);
                }
            }
        }
    }

    private void loadListData(boolean loadMore) {
        int page;
        if (!loadMore) {
            page = listCurPage = 1;
        } else {
            page = listCurPage + 1;
        }

        HttpRequest.getInstance()
                .getApiService()
                .getDiaryPage(page, 20)
                .compose(RxComposer.commonRefresh(this, rllList, loadMore))
                .subscribe(new SimpleObserver<PageResultDTO<Diary>>() {
                    @Override
                    public void onNext(PageResultDTO<Diary> response) {
                        listCurPage = page;
                        if (listCurPage == 1) {
                            listInfoList.clear();
                        }

                        listInfoList.addAll(response.getRecords());
                        rllList.setEnableLoadmore(response.getCurrent() < response.getPages());
                        rllList.checkEmpty();
                    }
                });
    }

    @OnClick(R.id.fab)
    public void onClick() {
        DiaryEditActivity.start(activity, null);
    }
}