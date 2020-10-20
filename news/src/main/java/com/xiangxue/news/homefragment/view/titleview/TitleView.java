package com.xiangxue.news.homefragment.view.titleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.xiangxue.news.R;
import com.xiangxue.news.databinding.TitleViewBinding;
import com.xiangxue.news.homefragment.view.IDataChangeListener;
import com.xiangxue.webview.WebviewActivity;

public class TitleView extends LinearLayout implements IDataChangeListener<TitleViewModel> {
    private TitleViewBinding mDataBinding;
    private TitleViewModel mTitleViewModel;
    public TitleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.title_view, this, false);
        View view = mDataBinding.getRoot();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                WebviewActivity.startCommonWeb(view.getContext(),"News",mTitleViewModel.navigateUrl);
            }
        });
        addView(view);
    }


    @Override
    public void setData(TitleViewModel viewModel) {
        if (viewModel != null){
            this.mTitleViewModel = viewModel;
            mDataBinding.setViewmodel(viewModel);
            mDataBinding.executePendingBindings();
        }
    }
}
