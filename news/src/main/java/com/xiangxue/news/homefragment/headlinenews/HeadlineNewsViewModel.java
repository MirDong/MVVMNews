package com.xiangxue.news.homefragment.headlinenews;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.mvvm.dzk.base.BaseApplication;
import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.mvvm.dzk.base.mvvm.model.PagingResult;
import com.xiangxue.news.homefragment.api.NewsChannelsBean;

import java.util.List;

public class HeadlineNewsViewModel implements IBaseModelListener<List<NewsChannelsBean.ChannelList>> {
    private HeadlineNewsModel mHeadlineNewsModel;
    public MutableLiveData<List<NewsChannelsBean.ChannelList>> channelListLiveData = new MutableLiveData<>();
    public HeadlineNewsViewModel() {
        mHeadlineNewsModel = new HeadlineNewsModel();
        mHeadlineNewsModel.register(this);
        mHeadlineNewsModel.getCachedDataAndLoad();
    }

    @Override
    public void onLoadSuccess(BaseMvvmModel baseMvvmModel, List<NewsChannelsBean.ChannelList> channelLists, PagingResult... results) {
        if (baseMvvmModel instanceof HeadlineNewsModel){
            channelListLiveData.postValue(channelLists);
        }

    }

    @Override
    public void onLoadFail(BaseMvvmModel baseMvvmModel, String message, PagingResult... results) {
        Toast.makeText(BaseApplication.sApplication,message,Toast.LENGTH_SHORT).show();
    }
}
