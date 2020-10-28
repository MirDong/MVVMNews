package com.xiangxue.news.homefragment.newslist;

import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.viewmodel.BaseMvvmViewModel;

public class NewsListViewModel extends BaseMvvmViewModel<NewsListModel,BaseViewModel> {
    private String mChannelId;
    private String mChannelName;
    public NewsListViewModel(String channelId,String channelName) {
        this.mChannelId = channelId;
        this.mChannelName = channelName;
    }


    @Override
    public NewsListModel createModel() {
        return new NewsListModel(mChannelId,mChannelName);
    }
}
