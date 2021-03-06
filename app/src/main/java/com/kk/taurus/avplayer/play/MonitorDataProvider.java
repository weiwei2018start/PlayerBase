package com.kk.taurus.avplayer.play;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.kk.taurus.avplayer.utils.DataUtils;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.provider.BaseDataProvider;
import com.kk.taurus.playerbase.provider.IDataProvider;

/**
 * Created by Taurus on 2018/4/15.
 */

public class MonitorDataProvider extends BaseDataProvider {

    private DataSource mDataSource;

    private int mRequestNum;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    public void handleSourceData(DataSource sourceData) {
        this.mDataSource = sourceData;
        onProviderDataStart();
        mHandler.postDelayed(mLoadDataRunnable, 2000);
    }

    private Runnable mLoadDataRunnable = new Runnable() {
        @Override
        public void run() {
            mRequestNum = mRequestNum%DataUtils.urls.length;
//            if(mRequestNum%2==1){
//                Bundle bundle = BundlePool.obtain();
//                bundle.putString(EventKey.STRING_DATA, "TestError");
//                onProviderError(888,bundle);
//                return;
//            }
            mDataSource.setData(DataUtils.urls[mRequestNum]);
            mRequestNum++;
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, mDataSource);
            onProviderDataSuccess(IDataProvider.PROVIDER_CODE_SUCCESS_MEDIA_DATA, bundle);
        }
    };

    @Override
    public void cancel() {
        mHandler.removeCallbacks(mLoadDataRunnable);
    }

    @Override
    public void destroy() {
        cancel();
    }
}
