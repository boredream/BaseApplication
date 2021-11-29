package com.boredream.baseapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.entity.SettingItem;
import com.boredream.baseapplication.listener.OnListSelectedListener;
import com.boredream.baseapplication.net.GlideHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingItemAdapter extends RecyclerView.Adapter<SettingItemAdapter.ViewHolder> {

    private List<SettingItem> infoList;
    private OnListSelectedListener<SettingItem> onListSelectedListener;

    public void setOnItemClickListener(OnListSelectedListener<SettingItem> onListSelectedListener) {
        this.onListSelectedListener = onListSelectedListener;
    }

    public SettingItemAdapter(List<SettingItem> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SettingItem data = infoList.get(position);

        if (data.getIcon() != null) {
            holder.ivLeft.setVisibility(View.VISIBLE);
            holder.ivLeft.setImageResource(data.getIcon());
        } else {
            holder.ivLeft.setVisibility(View.GONE);
        }
        holder.tvName.setText(data.getName());

        String rightText = data.getRightText();
        if (rightText != null) {
            holder.tvRight.setVisibility(View.VISIBLE);
            holder.tvRight.setText(rightText);
        } else {
            holder.tvRight.setVisibility(View.GONE);
        }

        String rightImage = data.getRightImage();
        if (rightImage != null) {
            holder.ivRight.setVisibility(View.VISIBLE);
            GlideHelper.loadOvalImg(holder.ivRight, rightImage);
        } else {
            holder.ivRight.setVisibility(View.GONE);
        }

        holder.ivRightArrow.setVisibility(data.isShowRightArrow() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (onListSelectedListener != null) {
                onListSelectedListener.onItemSelected(data);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
