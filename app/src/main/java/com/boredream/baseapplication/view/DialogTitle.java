package com.boredream.baseapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.boredream.baseapplication.R;

public class DialogTitle extends ConstraintLayout {

    private View close_view;
    private TextView title_tv;
    private View iconClose;
    private Button buttonClose;

    public DialogTitle(Context context) {
        super(context);
        init(null, 0);
    }

    public DialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DialogTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attributeSet, int defStyleAttr) {
        View.inflate(getContext(), R.layout.view_dialog_title, this);
        final TypedArray a = getContext().obtainStyledAttributes(attributeSet,
                R.styleable.DialogTitle, defStyleAttr, 0);
        title_tv = findViewById(R.id.title);
        title_tv.setText(a.getString(R.styleable.DialogTitle_title));
        iconClose = findViewById(R.id.icon_close);
        buttonClose = findViewById(R.id.button_close);
        buttonClose.setText(a.getString(R.styleable.DialogTitle_close_text));

        // 默认是按钮
        if (a.getInt(R.styleable.DialogTitle_close_style, 0) == 0) {
            close_view = buttonClose;
            iconClose.setVisibility(INVISIBLE);
        } else {
            close_view = iconClose;
            buttonClose.setVisibility(INVISIBLE);
        }
        a.recycle();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        close_view.setOnClickListener(l);
    }

    public void setTitle(String title) {
        title_tv.setText(title);
    }
}
