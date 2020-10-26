package com.mvvm.dzk.base.mvvm.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;

import java.util.List;

public abstract class BaseMvvmViewModel<MODEL extends BaseMvvmModel,DATA> extends ViewModel implements IBaseModelListener<List<DATA>> {
    public MutableLiveData<List<DATA>> dataList = new MutableLiveData<>();
    private MODEL model;
    public MutableLiveData<ViewStatus> viewStatusMutableLiveData = new MutableLiveData<>();
    public BaseMvvmViewModel(){

    }


    public void refresh(){
        viewStatusMutableLiveData.postValue(ViewStatus.LOADING);
        createAndRegisterModel();
        if (model != null){
            model.refresh();
        }
    }

    public void loadNextPage(){
        if (model != null){
            model.loadNextPage();
        }
    }

    public void getCacheAndLoad(){
        
    }

    public abstract MODEL createModel();
    private void createAndRegisterModel(){
        if (model == null){
            model = createModel();
        }
        if (model != null){
            model.register(this);
            model.getCachedDataAndLoad();
        }else {
            throw new NullPointerException("createModel method can not return null");
        }
    }
}
