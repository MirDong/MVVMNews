package com.xiangxue.news.homefragment.newslist;

import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.common.picturetitleview.PictureTitleViewModel;
import com.mvvm.dzk.common.titleview.TitleViewModel;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsListBean;

import java.util.ArrayList;
import java.util.List;

public class NewsListModel extends BaseMvvmModel<NewsListBean,List<BaseViewModel>> {
    private static final String CACHE_KEY = "cache_key_";
    private String mChannelId;
    private String mChannelName;
    public NewsListModel(String channelId,String channelName){
        super(true,CACHE_KEY+channelName+channelId,null,1);
        this.mChannelId = channelId;
        this.mChannelName = channelName;
    }

    /*public void refresh(){
        mPage = 1;
        loadNextPage();
    }*/

    @Override
    protected void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId, mChannelName, String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>(this,this)));
    }


    @Override
    public void onSuccess(boolean isFromCache, NewsListBean newsChannelsBean) {
        List<BaseViewModel> mViewModelList = new ArrayList<>();
        List<NewsListBean.Contentlist> contentlist = newsChannelsBean.showapiResBody.pagebean.contentlist;
        for (NewsListBean.Contentlist content:contentlist){
            if (content.imageurls!= null && content.imageurls.size() > 0){
                PictureTitleViewModel pictureTitleViewModel = new PictureTitleViewModel();
                pictureTitleViewModel.imageUrl = content.imageurls.get(0).url;
                pictureTitleViewModel.navigateUrl = content.link;
                pictureTitleViewModel.title = content.title;
                mViewModelList.add(pictureTitleViewModel);
            }else {
                TitleViewModel titleViewModel = new TitleViewModel();
                titleViewModel.title = content.title;
                titleViewModel.navigateUrl = content.link;
                mViewModelList.add(titleViewModel);
            }
        }
        notifyResultToListener(newsChannelsBean,mViewModelList,isFromCache);
    }

    @Override
    public void onFail(Throwable e) {
        loadFail(e.getMessage());
    }
}
