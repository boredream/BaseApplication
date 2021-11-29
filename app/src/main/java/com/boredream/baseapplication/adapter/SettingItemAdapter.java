package com.boredream.baseapplication.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.boredream.baseapplication.entity.SettingItem;
import com.boredream.baseapplication.listener.OnSelectedListener;
import com.boredream.baseapplication.view.SettingItemView;

import java.util.List;

public class SettingItemAdapter extends RecyclerView.Adapter<SettingItemAdapter.ViewHolder> {

    private List<SettingItem> infoList;
    private OnSelectedListener<SettingItem> onSelectedListener;

    public void setOnItemClickListener(OnSelectedListener<SettingItem> onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
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
        SettingItemView v = new SettingItemView(parent.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SettingItem data = infoList.get(position);
        holder.content.setData(data);

        holder.itemView.setOnClickListener(v -> {
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(data);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        SettingItemView content;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (SettingItemView) itemView;
        }
    }
}
