package com.xiangxue.news.homefragment.newslist;

import androidx.lifecycle.MutableLiveData;
import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.mvvm.dzk.base.mvvm.model.PagingResult;
import com.mvvm.dzk.base.mvvm.viewmodel.ViewStatus;

import java.util.ArrayList;
import java.util.List;

public class NewsListViewModel implements IBaseModelListener<List<BaseViewModel>>  {
    private NewsListModel mNewsListModel;
    public MutableLiveData<List<BaseViewModel>> viewModelList = new MutableLiveData<>();
    public MutableLiveData<ViewStatus> viewStatusMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String>errorMsg = new MutableLiveData<>();
    public NewsListViewModel(String channelId,String channelName) {
        viewModelList.setValue(new ArrayList<>());
        mNewsListModel = new NewsListModel(channelId,channelName);
        mNewsListModel.register(this);
        mNewsListModel.getCachedDataAndLoad();
    }

    public void refresh(){
        mNewsListModel.refresh();
    }

    public void loadNextPage(){
        mNewsListModel.loadNextPage();
    }

    @Override
    public void onLoadSuccess(BaseMvvmModel baseMvvmModel, List<BaseViewModel> baseViewModelList, PagingResult... results) {
        if (baseMvvmModel instanceof NewsListModel){
            if (results != null && results.length > 0 && results[0].isFirstPage){
                viewModelList.getValue().clear();
            }


            if (results[0].isEmpty){
                if (results[0].isFirstPage){
                    viewStatusMutableLiveData.postValue(ViewStatus.EMPTY);
                }else {
                    viewStatusMutableLiveData.postValue(ViewStatus.NO_MORE_DATA);
                }
            }else {
                viewModelList.getValue().addAll(baseViewModelList);
                viewModelList.postValue(viewModelList.getValue());
                viewStatusMutableLiveData.postValue(ViewStatus.SHOW_CONTENT);
            }
        }

    }

    @Override
    public void onLoadFail(BaseMvvmModel baseMvvmModel, String message, PagingResult... results) {
        errorMsg.postValue(message);
        if (results[0].isFirstPage){
            viewStatusMutableLiveData.postValue(ViewStatus.REFRESH_ERROR);
        }else {
            viewStatusMutableLiveData.postValue(ViewStatus.LOAD_MORE_FAIL);
        }
    }
}
