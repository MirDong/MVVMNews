package com.mvvm.dzk.base.mvvm.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.mvvm.dzk.base.BaseApplication;
import com.mvvm.dzk.base.R;
import com.mvvm.dzk.base.loadsir.CustomCallback;
import com.mvvm.dzk.base.loadsir.EmptyCallback;
import com.mvvm.dzk.base.loadsir.ErrorCallback;
import com.mvvm.dzk.base.loadsir.LoadingCallback;
import com.mvvm.dzk.base.loadsir.TimeoutCallback;
import com.mvvm.dzk.base.mvvm.viewmodel.BaseMvvmViewModel;
import com.mvvm.dzk.base.mvvm.viewmodel.ViewStatus;

import java.util.List;


public abstract class BaseMvvmFragment<VIEW extends ViewDataBinding,VIEWMODEL extends BaseMvvmViewModel,DATA> extends Fragment implements Observer {
    protected VIEW viewDataBinding;
    protected VIEWMODEL viewmodel;
    private LoadService mLoadService;
    protected abstract String getFragmentTag();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getFragmentTag(), "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(getFragmentTag(), "onCreateView: ");
        viewDataBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        //添加LoadSir
        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .build();
        mLoadService = loadSir.register(getLoadSirView(), new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                viewmodel.refresh();
            }
        });
        return viewDataBinding.getRoot();
    }
    protected abstract View getLoadSirView();
    protected abstract int getLayoutId();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(getFragmentTag(), "onViewCreated: ");
        viewmodel = getViewModel();
        getLifecycle().addObserver(viewmodel);
        viewmodel.dataList.observe(this,this);
        viewmodel.viewStatusLiveData.observe(this,this);
        onViewCreated();
        viewmodel.getCachedDataAndLoad();
    }
    protected abstract void onViewCreated();
    protected abstract VIEWMODEL getViewModel();

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus){
            switch ((ViewStatus)o){
                case EMPTY:
                    mLoadService.showCallback(EmptyCallback.class);
                    break;
                case LOADING:
                    mLoadService.showCallback(LoadingCallback.class);
                    break;
                case NO_MORE_DATA:
                    Toast.makeText(BaseApplication.sApplication, R.string.no_more_data,Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_CONTENT:
                    mLoadService.showSuccess();
                    break;
                case REFRESH_ERROR:
                    if (((List<DATA>)viewmodel.dataList.getValue()).size() == 0){
                        mLoadService.showCallback(ErrorCallback.class);
                    }else {
                        Toast.makeText(BaseApplication.sApplication,viewmodel.errorMessage.getValue().toString(),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case LOAD_MORE_FAIL:
                    Toast.makeText(BaseApplication.sApplication,viewmodel.errorMessage.getValue().toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
            onNetworkResponse(null,false);
        }else if (o instanceof List){
            onNetworkResponse((List<DATA>) o,true);
        }
    }

    protected abstract void onNetworkResponse(List<DATA> dataList,boolean isDataUpdated);

    protected void showLoading(){
        if (mLoadService != null){
            mLoadService.showCallback(LoadingCallback.class);
        }
    }
}
