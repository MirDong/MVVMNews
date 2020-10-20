package com.xiangxue.news.homefragment.view.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public View mItemView;
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mItemView = itemView;
    }
}
