package com.mvvm.dzk.base.mvvm.model;

public interface MvvmDataObserver<DATA> {
    void onSuccess(boolean isFromCache,DATA data);
    void onFail(Throwable e);
}
