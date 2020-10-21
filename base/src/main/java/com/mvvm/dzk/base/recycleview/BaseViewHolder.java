package com.mvvm.dzk.base.recycleview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.customview.IBaseCustomView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private IBaseCustomView mItemView;
    public BaseViewHolder(@NonNull IBaseCustomView itemView) {
        super((View) itemView);
        this.mItemView = itemView;
    }

    public void bind(BaseViewModel viewModel){
        mItemView.setData(viewModel);
    }
}
