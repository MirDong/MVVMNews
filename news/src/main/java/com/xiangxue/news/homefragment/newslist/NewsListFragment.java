package com.xiangxue.news.homefragment.newslist;

import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mvvm.dzk.base.customview.BaseViewModel;
import com.mvvm.dzk.base.mvvm.model.BaseMvvmModel;
import com.mvvm.dzk.base.mvvm.model.IBaseModelListener;
import com.mvvm.dzk.base.mvvm.model.PagingResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiangxue.news.R;
import com.xiangxue.news.databinding.FragmentNewsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NewsListFragment extends Fragment implements IBaseModelListener<List<BaseViewModel>> {
    private NewsListRecyclerViewAdapter mAdapter;
    private FragmentNewsBinding viewDataBinding;

    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";
    private NewsListModel mNewsListModel;
    private List<BaseViewModel>mViewModels = new ArrayList<>();
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

        mNewsListModel = new NewsListModel(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME));
        mNewsListModel.register(this);
        mNewsListModel.loadNextPage();
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNewsListModel.refresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mNewsListModel.loadNextPage();
            }
        });
        return viewDataBinding.getRoot();
    }


    @Override
    public void onLoadSuccess(BaseMvvmModel baseMvvmModel,List<BaseViewModel> baseViewModelList, PagingResult... results) {
        if (results != null && results.length > 0 && results[0].isFirstPage){
            mViewModels.clear();
        }
        mViewModels.addAll(baseViewModelList);
        mAdapter.setData(mViewModels);
        viewDataBinding.refreshLayout.finishRefresh();
        viewDataBinding.refreshLayout.finishLoadMore();
    }

    @Override
    public void onLoadFail(BaseMvvmModel baseMvvmModel,String message, PagingResult... results) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
