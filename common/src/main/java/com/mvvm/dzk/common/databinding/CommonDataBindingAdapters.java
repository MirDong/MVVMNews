package com.mvvm.dzk.common.databinding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CommonDataBindingAdapters {
    @BindingAdapter("loadImageUrl")
    public static void loadImageUrl(ImageView imageView, String url){
        Glide.with(imageView.getContext())
                .load(url)
                .transition(withCrossFade())
                .into(imageView);
    }
}
