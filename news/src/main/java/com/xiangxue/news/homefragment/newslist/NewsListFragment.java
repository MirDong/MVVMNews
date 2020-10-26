package com.xiangxue.news.homefragment.newslist;

import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.core.LoadService;
import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.viewmodel.ViewStatus;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.FragmentNewsBinding;

import java.util.List;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListFragment extends Fragment implements Observer{
    private static final String TAG = "NewsListFragment";
    private NewsListRecyclerViewAdapter mAdapter;
    private FragmentNewsBinding viewDataBinding;
    private LoadService mLoadService;
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";
    private NewsListViewModel mNewsListViewModel;

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        mAdapter = new NewsListRecyclerViewAdapter(getContext());
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.setAdapter(mAdapter);
        mNewsListViewModel = new NewsListViewModel(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME));

        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNewsListViewModel.refresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mNewsListViewModel.loadNextPage();
            }
        });
        mNewsListViewModel.viewModelList.observe(this, new Observer<List<BaseViewModel>>() {
            @Override
            public void onChanged(List<BaseViewModel> baseViewModelList) {
                mAdapter.setData(baseViewModelList);
                viewDataBinding.refreshLayout.finishRefresh();
                viewDataBinding.refreshLayout.finishLoadMore();
            }
        });

        mNewsListViewModel.errorMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Log.e(TAG, "errorMsg = " + message);
            }
        });
        return viewDataBinding.getRoot();
    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus){
//            switch (o)
        }
    }
}
