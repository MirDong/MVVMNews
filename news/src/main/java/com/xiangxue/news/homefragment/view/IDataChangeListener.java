package com.xiangxue.news.homefragment.view;


import com.xiangxue.news.homefragment.view.base.BaseViewModel;

public interface IDataChangeListener<VM extends BaseViewModel> {
    void setData(VM viewModel);
}
