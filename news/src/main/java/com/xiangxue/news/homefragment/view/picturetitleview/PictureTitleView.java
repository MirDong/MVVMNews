package com.xiangxue.news.homefragment.view.picturetitleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.PictureTitleViewBinding;
import com.xiangxue.news.homefragment.view.IDataChangeListener;
import com.xiangxue.news.homefragment.view.titleview.TitleViewModel;
import com.xiangxue.webview.WebviewActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PictureTitleView extends LinearLayout implements IDataChangeListener<PictureTitleViewModel> {
    private PictureTitleViewBinding mDataBinding;
    private PictureTitleViewModel mPictureTitleViewModel;
    public PictureTitleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.picture_title_view, this, false);
        View view = mDataBinding.getRoot();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                WebviewActivity.startCommonWeb(view.getContext(),"News", mPictureTitleViewModel.navigateUrl);
            }
        });
        addView(view);
    }


    @Override
    public void setData(PictureTitleViewModel viewModel) {
        if (viewModel != null){
            this.mPictureTitleViewModel = viewModel;
            mDataBinding.setViewmodel(viewModel);
            mDataBinding.executePendingBindings();
        }
    }

    @BindingAdapter("loadImageUrl")
    public static void loadImageUrl(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .transition(withCrossFade())
                .into(imageView);
    }
}
