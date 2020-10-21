package com.mvvm.dzk.base.mvvm.model;

public interface IBaseModelListener<DATA> {
    void onLoadSuccess(DATA data,PagingResult... results);
    void onLoadFail(String message);
}
