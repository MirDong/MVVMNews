package com.xiangxue.news.homefragment.headlinenews;

import android.util.Log;

import com.google.gson.Gson;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsChannelsBean;

import java.util.ArrayList;
import java.util.List;

public class HeadlineNewsModel {
    private IBaseModelListener<List<NewsChannelsBean.ChannelList>> mBaseModelListener;

    public HeadlineNewsModel(IBaseModelListener<List<NewsChannelsBean.ChannelList>> mBaseModelListener) {
        this.mBaseModelListener = mBaseModelListener;
    }

    public void load(){
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsChannels()
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsChannelsBean>() {
                    @Override
                    public void onSuccess(NewsChannelsBean newsChannelsBean) {
                        Log.e("MainActivity", new Gson().toJson(newsChannelsBean));
                        if (mBaseModelListener != null){
                            mBaseModelListener.onLoadSuccess(newsChannelsBean.showapiResBody.channelList);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        if (mBaseModelListener != null){
                            mBaseModelListener.onLoadFail(e.getMessage());
                        }
                    }
                }));
    }
}
