package com.boredream.baseapplication.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.boredream.baseapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleBar extends FrameLayout {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_iv_left)
    ImageView titleIvLeft;
    @BindView(R.id.title_tv_right)
    DrawableTextView titleTvRight;

    public DrawableTextView getTvRight() {
        return titleTvRight;
    }

    public TitleBar(Context context) {
        super(context);
        initView(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_titlebar, this);
        ButterKnife.bind(this);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        String aa = a.getString(R.styleable.TitleBar_title);
        titleTv.setText(aa);
        a.recycle();
    }

    public TitleBar setLeftMode() {
        titleTv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        return this;
    }

    public TitleBar setTitle(CharSequence title) {
        titleTv.setText(title);
        return this;
    }

    public TitleBar setLeftBack() {
        titleIvLeft.setVisibility(View.VISIBLE);
        titleIvLeft.setOnClickListener(v -> {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).finish();
            }
        });
        return this;
    }

    public TitleBar setRight(String text, OnClickListener listener) {
        return setRight(text, null, listener);
    }

    public TitleBar setRight(String text, Integer res, OnClickListener listener) {
        titleTvRight.setVisibility(VISIBLE);
        titleTvRight.setText(text);
        if (res != null) {
            titleTvRight.setDrawables(new Drawable[]{getResources().getDrawable(res), null, null, null});
        }
        titleTvRight.setOnClickListener(listener);
        return this;
    }

}
