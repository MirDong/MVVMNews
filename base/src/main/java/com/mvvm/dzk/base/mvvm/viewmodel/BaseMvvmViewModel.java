package com.mvvm.dzk.base.mvvm.viewmodel;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.mvvm.dzk.base.mvvm.model.PagingResult;

import java.util.List;

/**
 * @author jackie
 * @date 2020/10/28
 */
public abstract class BaseMvvmViewModel<MODEL extends BaseMvvmModel,DATA> extends ViewModel implements LifecycleObserver, IBaseModelListener<List<DATA>> {
    public MutableLiveData<List<DATA>> dataList = new MutableLiveData<>();
    public MutableLiveData<ViewStatus> viewStatusLiveData = new MutableLiveData();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    protected MODEL model;
    public BaseMvvmViewModel() {

    }
    public void refresh(){
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        createAndRegisterModel();
        if (model != null){
            model.refresh();
        }
    }

    public void getCachedDataAndLoad() {
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        createAndRegisterModel();
        if (model != null) {
            model.getCachedDataAndLoad();
        }
    }

    public void loadNextPage() {
        createAndRegisterModel();
        if (model != null) {
            model.loadNextPage();
        }
    }

    private void createAndRegisterModel(){
        if (model == null){
            model = createModel();
            if (model != null){
                model.register(this);
            }else {
                throw new NullPointerException("model is null");
            }
        }

    }

    public abstract MODEL createModel();
    @Override
    protected void onCleared() {
        super.onCleared();
        if (model != null){
            model.cancel();
        }
    }

    @Override
    public void onLoadSuccess(BaseMvvmModel baseMvvmModel, List<DATA> data, PagingResult... results) {

        if (baseMvvmModel.isPaging()){
            if (results[0].isEmpty){
                if (results[0].isFirstPage){
                    viewStatusLiveData.postValue(ViewStatus.EMPTY);
                }else {
                    viewStatusLiveData.postValue(ViewStatus.NO_MORE_DATA);
                }
            }else {
                if (results[0].isFirstPage){
                    dataList.postValue(data);
                }else {
                    dataList.getValue().addAll(data);
                    dataList.postValue(dataList.getValue());
                }
                viewStatusLiveData.postValue(ViewStatus.SHOW_CONTENT);
            }
        }else {
            dataList.postValue(data);
            viewStatusLiveData.postValue(ViewStatus.SHOW_CONTENT);
        }
    }

    @Override
    public void onLoadFail(BaseMvvmModel baseMvvmModel, String message, PagingResult... results) {
        errorMessage.postValue(message);
        if (results != null && results.length > 0){
            if (results[0].isFirstPage){
                viewStatusLiveData.postValue(ViewStatus.REFRESH_ERROR);
            }else {
                viewStatusLiveData.postValue(ViewStatus.LOAD_MORE_FAIL);
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume(){
        if (dataList == null || dataList.getValue() == null || dataList.getValue().size() == 0){
            createAndRegisterModel();
            if (model != null){
                model.getCachedDataAndLoad();
            }else {
                dataList.postValue(dataList.getValue());
                viewStatusLiveData.postValue(viewStatusLiveData.getValue());
            }
        }
    }
}
