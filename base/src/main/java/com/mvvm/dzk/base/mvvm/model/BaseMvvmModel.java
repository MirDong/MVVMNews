package com.mvvm.dzk.base.mvvm.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mvvm.dzk.base.preference.BasicDataPreferenceUtil;
import com.mvvm.dzk.base.utils.GenericUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseMvvmModel<NETWORK_DATA, CONVERTED_DATA> implements MvvmDataObserver<NETWORK_DATA> {
    private final String mPreDefineData;
    protected WeakReference<IBaseModelListener> mModelListenerWeakReference;
    protected int mPage = 1;
    private boolean mIsPaging;
    private boolean mIsLoading;
    private int INIT_PAGE_NUMBER;
    private String mCachePreferenceKey;
    private CompositeDisposable compositeDisposable;
    public BaseMvvmModel(boolean isPaging, String cachePreferenceKey, String preDefineData, int... initPageNumber) {
        this.mIsPaging = isPaging;
        this.mCachePreferenceKey = cachePreferenceKey;
        this.mPreDefineData = preDefineData;
        if (isPaging && initPageNumber != null && initPageNumber.length > 0) {
            INIT_PAGE_NUMBER = initPageNumber[0];
        } else {
            INIT_PAGE_NUMBER = -1;
        }
    }

    public void register(IBaseModelListener listener) {
        mModelListenerWeakReference = new WeakReference<>(listener);
    }

    public void refresh() {
        if (!mIsLoading) {
            if (mIsPaging) {
                mPage = INIT_PAGE_NUMBER;
            }
            mIsLoading = true;
            load();
        }
    }

    protected abstract void load();

    public void loadNextPage() {
        if (!mIsLoading) {
            mIsLoading = true;
            load();
        }
    }


    protected boolean isNeedToUpdate(long cachedTimeSlot) {
        return true;
    }

    public void getCachedDataAndLoad() {
        if (!mIsLoading) {
            mIsLoading = true;
            if (mCachePreferenceKey != null) {
                String cachedData = BasicDataPreferenceUtil.getInstance().getString(mCachePreferenceKey);
                if (!TextUtils.isEmpty(cachedData)) {
                    try {
                        NETWORK_DATA networkData = new Gson().fromJson(new JSONObject(cachedData).getString("data"), (Class<NETWORK_DATA>) GenericUtils.getGenericType(this));
                        if (networkData != null) {
                            onSuccess(true, networkData);
                        }
                        long timeSlot = Long.parseLong(new JSONObject(cachedData).getString("updateTimeInMills"));
                        if (isNeedToUpdate(timeSlot)) {
                            load();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (mPreDefineData != null) {
                    NETWORK_DATA savedData = new Gson().fromJson(mPreDefineData, (Class<NETWORK_DATA>) GenericUtils.getGenericType(this));
                    if (savedData != null) {
                        onSuccess(true, savedData);
                    }
                }
            }
            load();
        }
    }

    protected void notifyResultToListener(NETWORK_DATA networkData, CONVERTED_DATA convertedData) {
        IBaseModelListener listener = mModelListenerWeakReference.get();
        if (listener != null) {
            if (mIsPaging) {
                listener.onLoadSuccess(this, convertedData, new PagingResult(mPage == INIT_PAGE_NUMBER,
                        convertedData == null ? true : ((List) convertedData).isEmpty(), ((List) convertedData).size() >= 20));
                if (mCachePreferenceKey != null && mPage == INIT_PAGE_NUMBER) {
                    saveDataToPrefernce(networkData);
                }
            } else {
                listener.onLoadSuccess(this, convertedData);
                if (mCachePreferenceKey != null) {
                    saveDataToPrefernce(networkData);
                }
            }

            if (mIsPaging) {
                if (convertedData != null && ((List) convertedData).size() > 0) {
                    mPage++;
                }
            }
        }
        mIsLoading = false;
    }

    protected void loadFail(String message) {
        IBaseModelListener listener = mModelListenerWeakReference.get();
        if (listener != null) {
            if (mIsPaging) {
                listener.onLoadFail(this, message, new PagingResult(mPage == INIT_PAGE_NUMBER,
                        true, false));
            } else {
                listener.onLoadFail(this, message);
            }
        }
        mIsLoading = false;
    }

    private void saveDataToPrefernce(NETWORK_DATA networkData) {
        if (networkData != null) {
            BaseCacheData<NETWORK_DATA> cacheData = new BaseCacheData<>();
            cacheData.data = networkData;
            cacheData.updateTimeInMills = System.currentTimeMillis();
            BasicDataPreferenceUtil.getInstance().setString(mCachePreferenceKey, new Gson().toJson(cacheData));
        }
    }

    public void cancel(){
        if (compositeDisposable != null && !compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }
    public void addDisposable(Disposable d){
        if (d == null){
            return;
        }

        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(d);
    }
}
