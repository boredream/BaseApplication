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
import com.boredream.baseapplication.net.GlideHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingItemAdapter extends RecyclerView.Adapter<SettingItemAdapter.ViewHolder> {

    private List<SettingItem> infoList;
    private AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

        String right = data.getRightText();
        if (right != null) {
            if (right.startsWith("http")) {
                holder.ivRight.setVisibility(View.VISIBLE);
                holder.tvRight.setVisibility(View.GONE);
                GlideHelper.loadOvalImg(holder.ivRight, right);
            } else {
                holder.ivRight.setVisibility(View.GONE);
                holder.tvRight.setVisibility(View.VISIBLE);
                holder.tvRight.setText(right);
            }
        } else {
            holder.ivRight.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.GONE);
        }

        holder.ivRightArrow.setVisibility(data.isShowRightArrow() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(null, v, position, -1);
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
