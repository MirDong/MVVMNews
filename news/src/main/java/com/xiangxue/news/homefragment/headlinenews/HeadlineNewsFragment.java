package com.xiangxue.news.homefragment.headlinenews;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.mvvm.dzk.base.mvvm.model.PagingResult;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.FragmentHomeBinding;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsChannelsBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HeadlineNewsFragment extends Fragment implements IBaseModelListener<List<NewsChannelsBean.ChannelList>> {
    private static final String TAG = "HeadlineNewsFragment";
    public HeadlineNewsFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    private HeadlineNewsModel mHeadlineNewsModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        adapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
        mHeadlineNewsModel = new HeadlineNewsModel();
        mHeadlineNewsModel.register(this);
        mHeadlineNewsModel.loadNextPage();
        return viewDataBinding.getRoot();
    }

    @Override
    public void onLoadSuccess(BaseMvvmModel baseMvvmModel, List<NewsChannelsBean.ChannelList> channelLists, PagingResult... results) {
        adapter.setChannels(channelLists);
    }

    @Override
    public void onLoadFail(BaseMvvmModel baseMvvmModel,String message,PagingResult... results) {
        Log.e(TAG, "onLoadFail: "+ message);
    }
}
