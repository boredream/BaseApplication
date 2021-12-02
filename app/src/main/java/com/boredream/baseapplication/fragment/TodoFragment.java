package com.boredream.baseapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.CollectionUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.adapter.TodoGroupAdapter;
import com.boredream.baseapplication.base.BaseFragment;
import com.boredream.baseapplication.dialog.BottomInputDialog;
import com.boredream.baseapplication.entity.Todo;
import com.boredream.baseapplication.entity.TodoGroup;
import com.boredream.baseapplication.entity.event.TodoUpdateEvent;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.view.TitleBar;
import com.boredream.baseapplication.view.loading.RefreshListLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TodoFragment extends BaseFragment {

    View view;
    Unbinder unbinder;

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rll_list)
    RefreshListLayout rllList;
    @BindView(R.id.tv_progress)
    TextView tvProgress;

    private ArrayList<TodoGroup> infoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_todo, null);
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
    public void onEvent(TodoUpdateEvent event) {
        loadData();
    }

    private void initView() {
        titleBar.setTitle("清单")
                .setLeftMode()
                .setRight("创建清单", R.drawable.ic_add_oval_whte, v -> addTodoGroup());

        rllList.setEnableRefresh(true);
        rllList.setEnableLoadmore(false);
        rllList.setOnRefreshListener(refresh -> loadData());
        rllList.getRv().setLayoutManager(new LinearLayoutManager(activity));
        rllList.getRv().setAdapter(new TodoGroupAdapter(infoList));
    }

    private void addTodoGroup() {
        BottomInputDialog dialog = new BottomInputDialog(activity, "清单组名称", null, new OnSelectedListener<String>() {
            @Override
            public void onSelected(String data) {
                TodoGroup group = new TodoGroup();
                group.setName(data);
                HttpRequest.getInstance()
                        .getApiService()
                        .postTodoGroup(group)
                        .compose(RxComposer.commonProgress(TodoFragment.this))
                        .subscribe(new SimpleObserver<String>() {
                            @Override
                            public void onNext(String s) {
                                showTip("创建成功");
                                loadData();
                            }
                        });
            }
        });
        dialog.show();
    }

    private void initData() {
        loadData();
    }

    private void loadData() {
        HttpRequest.getInstance()
                .getApiService()
                .getTodoGroup()
                .compose(RxComposer.commonRefresh(this, rllList, false))
                .subscribe(new SimpleObserver<List<TodoGroup>>() {
                    @Override
                    public void onNext(List<TodoGroup> response) {
                        int totalCount = 0;
                        int progressCount = 0;
                        for (TodoGroup group : response) {
                            if (CollectionUtils.isEmpty(group.getTodoList())) continue;
                            for (Todo todo : group.getTodoList()) {
                                totalCount++;
                                if (todo.isDone()) {
                                    progressCount++;
                                }
                            }
                        }
                        tvProgress.setText(String.format(Locale.getDefault(), "已打卡%d/%d", progressCount, totalCount));

                        infoList.clear();
                        infoList.addAll(response);
                        rllList.checkEmpty();
                    }
                });
    }
}