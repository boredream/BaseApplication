package com.boredream.baseapplication.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.blankj.utilcode.util.SizeUtils;
import com.boredream.baseapplication.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;


public final class DefaultMonthView extends MonthView {

    private Paint mCusTextPaint = new Paint();
    private Paint mCusOtherTextPaint = new Paint();
    private Paint mCusSelectTextPaint = new Paint();
    private Paint mCusSelectBgPaint = new Paint();
    private Paint mCusHintPaint = new Paint();

    public DefaultMonthView(Context context) {
        super(context);
    }

    /**
     * 绘制选中的日期
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 是否绘制onDrawScheme，true or false
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        float cx = x + 0.5f *  mItemWidth;
        float cy = y + 0.5f *  mItemHeight;
        float radius = 0.5f * mItemWidth;
        canvas.drawCircle(cx, cy, radius, mCusSelectBgPaint);
        return true;
    }

    /**
     * 绘制标记的日期,这里可以是背景色，标记色什么的
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        float hintX = x + 0.5f * mItemWidth - SizeUtils.dp2px(6f);
        float hintY = y + 1f * mItemHeight / 6 + mTextBaseLine - SizeUtils.dp2px(4f);
        float radius = SizeUtils.dp2px(1.5f);
        canvas.drawCircle(hintX, hintY, radius, mCusHintPaint);
    }

    /**
     * 绘制日历文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        int top = y - mItemHeight / 6;

        String text = calendar.isCurrentDay() ? "今" : String.valueOf(calendar.getDay());
        if (isSelected) {
            canvas.drawText(text, cx, mTextBaseLine + top, mCusSelectTextPaint);
        } else {
            canvas.drawText(text, cx, mTextBaseLine + top, calendar.isCurrentMonth() ? mCusTextPaint : mCusOtherTextPaint);
        }
    }

    /**
     * 开始绘制前的钩子，这里做一些初始化的操作，每次绘制只调用一次，性能高效
     * 没有需要可忽略不实现
     * 例如：
     * 1、需要绘制圆形标记事件背景，可以在这里计算半径
     * 2、绘制矩形选中效果，也可以在这里计算矩形宽和高
     */
    @Override
    protected void onPreviewHook() {
        mCusTextPaint.setAntiAlias(true);
        mCusTextPaint.setStyle(Paint.Style.FILL);
        mCusTextPaint.setTextAlign(Paint.Align.CENTER);
        mCusTextPaint.setColor(getResources().getColor(R.color.txt_gray));
        mCusTextPaint.setTextSize(SizeUtils.dp2px(14));

        mCusOtherTextPaint.setAntiAlias(true);
        mCusOtherTextPaint.setStyle(Paint.Style.FILL);
        mCusOtherTextPaint.setTextAlign(Paint.Align.CENTER);
        mCusOtherTextPaint.setColor(0xFFBBBBBB);
        mCusOtherTextPaint.setTextSize(SizeUtils.dp2px(14));

        mCusSelectTextPaint.setAntiAlias(true);
        mCusSelectTextPaint.setStyle(Paint.Style.FILL);
        mCusSelectTextPaint.setTextAlign(Paint.Align.CENTER);
        mCusSelectTextPaint.setColor(getResources().getColor(R.color.white));
        mCusSelectTextPaint.setTextSize(SizeUtils.dp2px(14));

        mCusSelectBgPaint.setAntiAlias(true);
        mCusSelectBgPaint.setStyle(Paint.Style.FILL);
        mCusSelectBgPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mCusHintPaint.setAntiAlias(true);
        mCusHintPaint.setStyle(Paint.Style.FILL);
        mCusHintPaint.setColor(getResources().getColor(R.color.red));
    }
}
