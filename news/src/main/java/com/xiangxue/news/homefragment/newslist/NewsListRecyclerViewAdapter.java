package com.xiangxue.news.homefragment.newslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xiangxue.news.R;
import com.xiangxue.news.homefragment.api.NewsListBean;
import com.xiangxue.news.homefragment.view.base.BaseViewHolder;
import com.xiangxue.news.homefragment.view.base.BaseViewModel;
import com.xiangxue.news.homefragment.view.picturetitleview.PictureTitleView;
import com.xiangxue.news.homefragment.view.picturetitleview.PictureTitleViewModel;
import com.xiangxue.news.homefragment.view.titleview.TitleView;
import com.xiangxue.news.homefragment.view.IDataChangeListener;
import com.xiangxue.news.homefragment.view.titleview.TitleViewModel;
import com.xiangxue.webview.WebviewActivity;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final int VIEW_TYPE_PICTURE_TITLE = 1;
    private final int VIEW_TYPE_TITLE = 2;
    private List<BaseViewModel> mItems;
    private Context mContext;

    NewsListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    void setData(List<BaseViewModel> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems != null && mItems.get(position) instanceof PictureTitleViewModel) {
            return VIEW_TYPE_PICTURE_TITLE;
        }
        return VIEW_TYPE_TITLE;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PICTURE_TITLE) {
            return new BaseViewHolder(new PictureTitleView(mContext));
        } else if (viewType == VIEW_TYPE_TITLE) {
            return new BaseViewHolder(new TitleView(mContext));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            ((IDataChangeListener) holder.mItemView).setData(mItems.get(position));
    }
}
