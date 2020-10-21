package com.mvvm.dzk.base.mvvm.model;

public interface IBaseModelListener<DATA> {
    void onLoadSuccess(BaseMvvmModel baseMvvmModel,DATA data,PagingResult... results);
    void onLoadFail(BaseMvvmModel baseMvvmModel,String message,PagingResult... results);
}
