package com.boredream.baseapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.boredream.baseapplication.R;
import com.boredream.baseapplication.entity.TheDay;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TheDayAdapter extends RecyclerView.Adapter<TheDayAdapter.ViewHolder> {

    private List<TheDay> infoList;
    private OnSelectedListener<TheDay> onSelectedListener;

    public void setOnItemClickListener(OnSelectedListener<TheDay> onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    public TheDayAdapter(List<TheDay> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_the_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TheDay data = infoList.get(position);
        holder.tvName.setText(data.getName());
        String date = data.getTheDayDate();
        if (StringUtils.isEmpty(date)) {
            holder.ivAdd.setVisibility(View.VISIBLE);
            holder.tvNotifyType.setVisibility(View.GONE);
            holder.tvDayCount.setVisibility(View.GONE);
            holder.tv.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvName.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_VERTICAL);

            holder.tvDate.setVisibility(View.GONE);
        } else {
            holder.ivAdd.setVisibility(View.GONE);
            holder.tvNotifyType.setVisibility(View.VISIBLE);
            holder.tvDayCount.setVisibility(View.VISIBLE);
            holder.tv.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvName.getLayoutParams();
            params.removeRule(RelativeLayout.CENTER_VERTICAL);

            holder.tvDate.setVisibility(View.VISIBLE);
            try {
                date = DateUtils.calendar2str(DateUtils.str2calendar(data.getTheDayDate()), "yyyy/MM/dd（EEEE）");
            } catch (Exception e) {
                date = data.getTheDayDate();
            }
            holder.tvDate.setText(date);
        }

        holder.itemView.setOnClickListener(v -> {
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(data);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.tv_day_count)
        TextView tvDayCount;
        @BindView(R.id.tv_notify_type)
        TextView tvNotifyType;
        @BindView(R.id.iv_add)
        ImageView ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
