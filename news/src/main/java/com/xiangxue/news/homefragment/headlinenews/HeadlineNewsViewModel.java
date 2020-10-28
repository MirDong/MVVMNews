package com.xiangxue.news.homefragment.headlinenews;

import com.mvvm.dzk.base.mvvm.viewmodel.BaseMvvmViewModel;
import com.xiangxue.news.homefragment.api.NewsChannelsBean;


public class HeadlineNewsViewModel extends BaseMvvmViewModel<HeadlineNewsModel,NewsChannelsBean.ChannelList> {
    @Override
    public HeadlineNewsModel createModel() {
        return new HeadlineNewsModel();
    }
}
