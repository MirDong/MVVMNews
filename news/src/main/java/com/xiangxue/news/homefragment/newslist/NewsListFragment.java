package com.xiangxue.news.homefragment.newslist;

import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.view.BaseMvvmFragment;
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
public class NewsListFragment extends BaseMvvmFragment<FragmentNewsBinding,NewsListViewModel,BaseViewModel> {
    private NewsListRecyclerViewAdapter mAdapter;

    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getFragmentTag() {
        return "NewsListFragment";
    }


    @Override
    protected View getLoadSirView() {
        return viewDataBinding.refreshLayout;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void onViewCreated() {
        mAdapter = new NewsListRecyclerViewAdapter(getContext());
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.setAdapter(mAdapter);
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewmodel.refresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewmodel.loadNextPage();
            }
        });
//        showLoading();
    }

    @Override
    protected NewsListViewModel getViewModel() {
//        mNewsListViewModel = new NewsListViewModel(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME));
        return new ViewModelProvider(getActivity(),new NewsListViewModel.NewsListViewModelFactory(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),
                getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME))).get(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),NewsListViewModel.class);
    }

    @Override
    protected void onNetworkResponse(List<BaseViewModel> baseViewModelList, boolean isDataUpdated) {
        viewDataBinding.refreshLayout.finishRefresh();
        viewDataBinding.refreshLayout.finishLoadMore();
        if (isDataUpdated){
            mAdapter.setData(baseViewModelList);
        }
    }
}
