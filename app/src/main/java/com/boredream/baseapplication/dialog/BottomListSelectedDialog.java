package com.boredream.baseapplication.dialog;


import android.content.Context;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.listener.OnListSelectedListener;
import com.boredream.baseapplication.view.DialogTitle;
import com.boredream.baseapplication.view.WheelView;

import java.io.Serializable;
import java.util.ArrayList;

public class BottomListSelectedDialog<T extends Serializable> extends BottomDialog {

    private DialogTitle dialogTitle;
    private WheelView<T> wheel;
    private final String title;
    private final ArrayList<T> items;
    private final T selectedItem;

    private OnListSelectedListener<T> listener;
    private boolean autoDismiss;

    public void setOnListSelectedListener(OnListSelectedListener<T> listener) {
        this.listener = listener;
    }

    public BottomListSelectedDialog(Context context, String title, ArrayList<T> items, T selectedItem) {
        super(context);
        this.title = title;
        this.items = items;
        this.selectedItem = selectedItem;

        setContentView(R.layout.dialog_bottom_list);
        initView();
        initData();
    }

    private void initView() {
        dialogTitle = findViewById(R.id.bottom_dialog_title);
        wheel = findViewById(R.id.bottom_dialog_wheel);
        dialogTitle.setOnClickListener(v -> performClick());
    }

    private void initData() {
        this.dialogTitle.setTitle("请选择" + (title == null ? "" : title));
        wheel.setItems(items, selectedItem);
    }

    private void performClick() {
        if (autoDismiss) dismiss();
        T selectItem = wheel.getSelectItem();
        if (listener != null) listener.onItemSelected(selectItem);
        dismiss();
    }

}
