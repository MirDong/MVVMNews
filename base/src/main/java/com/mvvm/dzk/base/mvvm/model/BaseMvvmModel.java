package com.mvvm.dzk.base.mvvm.model;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class BaseMvvmModel<NETWORK_DATA, CONVERTED_DATA> {
    protected WeakReference<IBaseModelListener>mModelListenerWeakReference;
    protected int mPage = 1;
    private boolean mIsPaging;
    private boolean mIsLoading;
    private int INIT_PAGE_NUMBER;
    private String mCachePreferenceKey;
    public BaseMvvmModel(boolean isPaging,String cachePreferenceKey,int... initPageNumber){
        this.mIsPaging = isPaging;
        this.mCachePreferenceKey = cachePreferenceKey;
        if (isPaging && initPageNumber != null && initPageNumber.length > 0){
            INIT_PAGE_NUMBER = initPageNumber[0];
        }else {
            INIT_PAGE_NUMBER = -1;
        }
    }
    public void register(IBaseModelListener listener){
        mModelListenerWeakReference = new WeakReference<>(listener);
    }
    public void refresh(){
        if (!mIsLoading){
            if (mIsPaging){
                mPage = INIT_PAGE_NUMBER;
            }
            mIsLoading = true;
            load();
        }
    }

    protected abstract void load();

    public void loadNextPage(){
        if (!mIsLoading){
            mIsLoading = true;
            load();
        }
    }

    protected void notifyResultToListener(NETWORK_DATA networkData, CONVERTED_DATA convertedData){
        IBaseModelListener listener = mModelListenerWeakReference.get();
        if (listener != null){
            if (mIsPaging){
                listener.onLoadSuccess(this,convertedData,new PagingResult(mPage == INIT_PAGE_NUMBER,
                                convertedData == null?true:((List)convertedData).isEmpty(),((List)convertedData).size() >= 20));
                if (mCachePreferenceKey != null && mPage == INIT_PAGE_NUMBER){
                    saveDataToPrefernce(networkData);
                }
            } else {
                listener.onLoadSuccess(this,convertedData);
                if (mCachePreferenceKey != null){
                    saveDataToPrefernce(networkData);
                }
            }

            if (mIsPaging){
                if (convertedData != null && ((List)convertedData).size() > 0){
                    mPage++;
                }
            }
        }
        mIsLoading = false;
    }

    protected void loadFail(String message){
        IBaseModelListener listener = mModelListenerWeakReference.get();
        if (listener != null){
            if (mIsPaging){
                listener.onLoadFail(this,message,new PagingResult(mPage == INIT_PAGE_NUMBER,
                        true,false));
            }else {
                listener.onLoadFail(this,message);
            }
        }
        mIsLoading = false;
    }
    private void saveDataToPrefernce(NETWORK_DATA networkData) {
        if (networkData != null){

        }
    }
}
