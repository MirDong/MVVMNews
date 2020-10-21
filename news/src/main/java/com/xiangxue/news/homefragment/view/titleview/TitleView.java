package com.xiangxue.news.homefragment.view.titleview;

import android.content.Context;
import com.mvvm.dzk.base.customview.BaseCustomView;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.TitleViewBinding;
import com.xiangxue.webview.WebviewActivity;

public class TitleView extends BaseCustomView<TitleViewBinding,TitleViewModel> {
    public TitleView(Context context) {
        super(context);
    }

    @Override
    protected void setDataToView(TitleViewModel viewModel) {
        mDataBinding.setViewmodel(viewModel);
    }

    @Override
    protected void onRootViewClicked() {
        WebviewActivity.startCommonWeb(getContext(),"News",mViewModel.navigateUrl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.title_view;
    }
}
