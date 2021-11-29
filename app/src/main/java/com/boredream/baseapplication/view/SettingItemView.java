package com.boredream.baseapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.dialog.BottomListSelectedDialog;
import com.boredream.baseapplication.dialog.WheelDatePickDialog;
import com.boredream.baseapplication.entity.SettingItem;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.net.GlideHelper;
import com.boredream.baseapplication.utils.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingItemView extends RelativeLayout {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_right_arrow)
    ImageView ivRightArrow;

    private View dividerView;

    public SettingItemView(Context context) {
        super(context);
        initView(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_setting_item, this);
        ButterKnife.bind(this);

        dividerView = new View(context);
        dividerView.setBackgroundResource(R.color.divider_gray);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        params.leftMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        params.addRule(ALIGN_PARENT_BOTTOM);
        dividerView.setLayoutParams(params);
        addView(dividerView);

        if (attrs == null) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        int icon = ta.getResourceId(R.styleable.SettingItemView_icon, -1);
        String name = ta.getString(R.styleable.SettingItemView_name);
        String rightText = ta.getString(R.styleable.SettingItemView_rightText);
        String rightImage = ta.getString(R.styleable.SettingItemView_rightImage);
        boolean showRightArrow = ta.getBoolean(R.styleable.SettingItemView_showRightArrow, false);
        setData(new SettingItem(icon == -1 ? null : icon, name, rightText, rightImage, showRightArrow));
    }

    public void setName(String str) {
        tvName.setText(str);
    }

    public void setText(String str) {
        tvRight.setText(str);
    }

    public String getText() {
        return tvRight.getText().toString();
    }

    public void setData(SettingItem data) {
        if (data.getIcon() != null) {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setImageResource(data.getIcon());
        } else {
            ivLeft.setVisibility(View.GONE);
        }
        tvName.setText(data.getName());

        String rightText = data.getRightText();
        if (rightText != null) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(rightText);
        } else {
            tvRight.setVisibility(View.GONE);
        }

        String rightImage = data.getRightImage();
        if (rightImage != null) {
            ivRight.setVisibility(View.VISIBLE);
            GlideHelper.loadOvalImg(ivRight, rightImage);
        } else {
            ivRight.setVisibility(View.GONE);
        }

        ivRightArrow.setVisibility(data.isShowRightArrow() ? View.VISIBLE : View.GONE);
    }

    public void setDateAction(OnSelectedListener<String> listener) {
        String oldData = tvRight.getText().toString().trim();
        if (StringUtils.isEmpty(oldData)) {
            oldData = null;
        }
        WheelDatePickDialog dialog = new WheelDatePickDialog(getContext(), oldData);
        dialog.setListener(calendar -> {
            String date = DateUtils.calendar2str(calendar);
            if (listener != null) {
                listener.onSelected(date);
            }
        });
        dialog.show();
    }

    public void setSpinnerAction(OnSelectedListener<String> listener, String... items) {
        setSpinnerAction(listener, new ArrayList<>(Arrays.asList(items)));
    }

    public void setSpinnerAction(OnSelectedListener<String> listener, ArrayList<String> list) {
        String oldData = tvRight.getText().toString().trim();
        if (StringUtils.isEmpty(oldData)) {
            oldData = null;
        }

        BottomListSelectedDialog<String> dialog = new BottomListSelectedDialog<>(
                getContext(), tvName.getText().toString().trim(), list, oldData);
        dialog.setOnListSelectedListener(data -> {
            if (listener != null) {
                listener.onSelected(data);
            }
        });
        dialog.show();
    }

}