package com.mvvm.dzk.base.customview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseCustomView<VIEW extends ViewDataBinding,DATA extends BaseViewModel> extends LinearLayout implements IBaseCustomView<DATA> {
    protected VIEW mDataBinding;
    protected DATA mViewModel;
    public BaseCustomView(Context context) {
        super(context);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), this, false);
        View view = mDataBinding.getRoot();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onRootViewClicked();
            }
        });
        addView(view);
    }

    @Override
    public void setData(DATA viewModel) {
        this.mViewModel = viewModel;
        setDataToView(viewModel);
        mDataBinding.executePendingBindings();

    }

    protected abstract void setDataToView(DATA viewModel);

    protected abstract void onRootViewClicked();

    protected abstract int getLayoutId();
}
