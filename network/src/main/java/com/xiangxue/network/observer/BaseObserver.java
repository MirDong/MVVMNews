package com.xiangxue.network.observer;

import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.MvvmDataObserver;
import com.xiangxue.network.errorhandler.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public  class BaseObserver<T> implements Observer<T> {
    private BaseMvvmModel mBaseModel;
    private MvvmDataObserver<T>mvvmDataObserver;

    public BaseObserver(BaseMvvmModel baseModel, MvvmDataObserver<T> mvvmDataObserver) {
        this.mBaseModel = baseModel;
        this.mvvmDataObserver = mvvmDataObserver;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mBaseModel != null){
            mBaseModel.addDisposable(d);
        }
    }

    @Override
    public void onNext(T t) {
        if (mvvmDataObserver != null){
            mvvmDataObserver.onSuccess(false,t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mvvmDataObserver == null){
            return;
        }
        if (e instanceof ExceptionHandle.ResponeThrowable){
            mvvmDataObserver.onFail(e);
        }else {
            mvvmDataObserver.onFail(new ExceptionHandle.ResponeThrowable(e,ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onComplete() {

    }
}
