package com.boredream.baseapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.StringUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.base.BaseActivity;
import com.boredream.baseapplication.base.BaseResponse;
import com.boredream.baseapplication.entity.Diary;
import com.boredream.baseapplication.entity.event.DiaryUpdateEvent;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;
import com.boredream.baseapplication.utils.DialogUtils;
import com.boredream.baseapplication.view.EditTextWithClear;
import com.boredream.baseapplication.view.SettingItemView;
import com.boredream.baseapplication.view.TitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class DiaryEditActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.etwc_name)
    EditTextWithClear etwcName;
    @BindView(R.id.siv_date)
    SettingItemView sivDate;
    @BindView(R.id.btn_delete)
    Button btnDelete;

    private Diary info;
    private boolean isEdit;

    public static void start(Context context, Diary info) {
        Intent intent = new Intent(context, DiaryEditActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);
        ButterKnife.bind(this);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        info = (Diary) getIntent().getSerializableExtra("info");
        isEdit = info != null;
        if (info == null) {
            info = new Diary();
        }
    }

    private void initView() {
        titleBar.setTitle(isEdit ? "修改日记" : "添加日记")
                .setLeftBack()
                .setRight("完成", v -> commit());
        sivDate.setDateAction(date -> info.setDiaryDate(date));
        btnDelete.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        btnDelete.setOnClickListener(v -> delete());
    }

    private void initData() {
        if (!isEdit) {
            return;
        }
        etwcName.setText(info.getContent());
        sivDate.setText(info.getDiaryDate());

        // TODO: chunyang 11/30/21 image
    }

    private void commit() {
        String name = etwcName.getText().toString().trim();
        if (StringUtils.isEmpty(name)) {
            showTip("内容不能为空");
            return;
        }
        info.setContent(name);

        Observable<BaseResponse<String>> observable;
        if (isEdit) {
            observable = HttpRequest.getInstance()
                    .getApiService()
                    .putDiary(info.getId(), info);
        } else {
            observable = HttpRequest.getInstance()
                    .getApiService()
                    .postDiary(info);
        }

        observable.compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        showTip("提交成功");
                        EventBus.getDefault().post(new DiaryUpdateEvent());
                        finish();
                    }
                });
    }

    private void delete() {
        DialogUtils.show2BtnDialog(this, "删除此条记录", "删除后无法恢复，请问确认删除吗？",
                v -> HttpRequest.getInstance()
                        .getApiService()
                        .deleteDiary(info.getId())
                        .compose(RxComposer.commonProgress(DiaryEditActivity.this))
                        .subscribe(new SimpleObserver<String>() {
                            @Override
                            public void onNext(String s) {
                                showTip("删除成功");
                                EventBus.getDefault().post(new DiaryUpdateEvent());
                                finish();
                            }
                        }));
    }

}