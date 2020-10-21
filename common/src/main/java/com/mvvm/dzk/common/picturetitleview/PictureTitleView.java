package com.mvvm.dzk.common.picturetitleview;

import android.content.Context;
import com.mvvm.dzk.base.customview.BaseCustomView;
import com.mvvm.dzk.common.R;
import com.mvvm.dzk.common.databinding.PictureTitleViewBinding;
import com.xiangxue.webview.WebviewActivity;



public class PictureTitleView extends BaseCustomView<PictureTitleViewBinding,PictureTitleViewModel> {
    public PictureTitleView(Context context) {
        super(context);
    }

    @Override
    protected void setDataToView(PictureTitleViewModel viewModel) {
        mDataBinding.setViewmodel(viewModel);
    }

    @Override
    protected void onRootViewClicked() {
        WebviewActivity.startCommonWeb(getContext(),"News", mViewModel.navigateUrl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.picture_title_view;
    }
}
