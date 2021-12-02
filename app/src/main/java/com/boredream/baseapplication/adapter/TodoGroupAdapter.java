package com.boredream.baseapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boredream.baseapplication.R;
import com.boredream.baseapplication.entity.Todo;
import com.boredream.baseapplication.entity.TodoGroup;
import com.boredream.baseapplication.net.GlideHelper;
import com.boredream.baseapplication.utils.DateUtils;
import com.boredream.baseapplication.view.RingLineView;
import com.boredream.baseapplication.view.decoration.GridDecoration;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoGroupAdapter extends RecyclerView.Adapter<TodoGroupAdapter.ViewHolder> {

    private List<TodoGroup> infoList;

    public TodoGroupAdapter(List<TodoGroup> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TodoGroup data = infoList.get(position);
        String name = data.getName();
        if ("爱的初体验".equals(name)) {
            holder.ivGroupIcon.setImageResource(R.drawable.ic_todo_group1);
        } else {
            holder.ivGroupIcon.setImageResource(R.drawable.ic_todo_group2);
        }
        holder.tvGroupName.setText(name);
        holder.ivGroupMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: chunyang 12/2/21
            }
        });

        List<Todo> todoList = data.getTodoList();
        int totalSize = todoList.size();
        int progress = 0;
        for (Todo todo : todoList) {
            if (todo.isDone()) {
                progress++;
            }
        }
        holder.tvGroupProgress.setText(String.format(Locale.getDefault(), "%d/%d", progress, totalSize));
        GridLayoutManager layoutManager = new GridLayoutManager(holder.itemView.getContext(), 4);
        holder.rvTodoList.setLayoutManager(layoutManager);
        holder.rvTodoList.addItemDecoration(new GridDecoration());
        holder.rvTodoList.setAdapter(new TodoAdapter(todoList));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_group_icon)
        ImageView ivGroupIcon;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.tv_group_progress)
        TextView tvGroupProgress;
        @BindView(R.id.iv_group_more)
        ImageView ivGroupMore;
        @BindView(R.id.rv_todo_list)
        RecyclerView rvTodoList;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
