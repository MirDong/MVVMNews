package com.mvvm.dzk.common.picturetitleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;
import com.mvvm.dzk.base.customview.BaseCustomView;
import com.mvvm.dzk.common.R;
import com.mvvm.dzk.common.databinding.PictureTitleViewBinding;
import com.xiangxue.webview.WebviewActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


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

    @BindingAdapter("loadImageUrl")
    public static void loadImageUrl(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .transition(withCrossFade())
                .into(imageView);
    }
}
