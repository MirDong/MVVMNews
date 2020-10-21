package com.xiangxue.news.homefragment.newslist;

import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.mvvm.dzk.base.mvvm.model.PagingResult;
import com.mvvm.dzk.common.picturetitleview.PictureTitleViewModel;
import com.mvvm.dzk.common.titleview.TitleViewModel;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsListBean;

import java.util.ArrayList;
import java.util.List;

public class NewsListModel {
    private IBaseModelListener<List<BaseViewModel>> mBaseModelListener;
    private String mChannelId;
    private String mChannelName;
    private int mPage = 1;
    public NewsListModel(IBaseModelListener<List<BaseViewModel>> listener,String channelId,String channelName){
        this.mBaseModelListener = listener;
        this.mChannelId = mChannelId;
        this.mChannelName = channelName;
    }

    public void refresh(){
        mPage = 1;
        loadNextPage();
    }

    public void loadNextPage(){
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId, mChannelName, String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>() {
                    @Override
                    public void onSuccess(NewsListBean newsChannelsBean) {
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
                        if (mBaseModelListener != null){
                            mBaseModelListener.onLoadSuccess(mViewModelList,new PagingResult(mPage == 1,mViewModelList.isEmpty(),mViewModelList.size() >= 20));
                        }
                        mPage ++;
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
