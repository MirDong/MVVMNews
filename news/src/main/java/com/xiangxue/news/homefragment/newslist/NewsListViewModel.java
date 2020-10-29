package com.xiangxue.news.homefragment.newslist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.viewmodel.BaseMvvmViewModel;

public class NewsListViewModel extends BaseMvvmViewModel<NewsListModel,BaseViewModel> {
    private String mChannelId;
    private String mChannelName;
    public NewsListViewModel(String channelId,String channelName) {
        this.mChannelId = channelId;
        this.mChannelName = channelName;
    }

    public static class NewsListViewModelFactory implements ViewModelProvider.Factory{
        private String mChannelId;
        private String mChannelName;

        public NewsListViewModelFactory(String channelId,String channelName){
            this.mChannelId = channelId;
            this.mChannelName = channelName;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new NewsListViewModel(mChannelId,mChannelName);
        }
    }

    @Override
    public NewsListModel createModel() {
        return new NewsListModel(mChannelId,mChannelName);
    }
}
