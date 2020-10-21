package com.xiangxue.news.homefragment.headlinenews;

import android.util.Log;

import com.google.gson.Gson;
import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsChannelsBean;

import java.util.List;


public class HeadlineNewsModel extends BaseMvvmModel<NewsChannelsBean, List<NewsChannelsBean.ChannelList>> {
    private static final String CACHE_KEY_HEADLINE_NEWS = "cache_key_headline_news";
    public HeadlineNewsModel() {
        super(false,CACHE_KEY_HEADLINE_NEWS);
    }

    @Override
    public void load(){
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsChannels()
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsChannelsBean>() {
                    @Override
                    public void onSuccess(NewsChannelsBean newsChannelsBean) {
                        Log.e("MainActivity", new Gson().toJson(newsChannelsBean));
                        notifyResultToListener(newsChannelsBean,newsChannelsBean.showapiResBody.channelList);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        loadFail(e.getMessage());
                    }
                }));
    }
}
