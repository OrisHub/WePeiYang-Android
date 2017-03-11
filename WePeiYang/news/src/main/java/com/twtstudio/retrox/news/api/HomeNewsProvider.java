package com.twtstudio.retrox.news.api;

import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 26/02/2017.
 */

public class HomeNewsProvider {
    private NewsApi newsApi;
    private NewsCacheApi newsCacheApi;


    public HomeNewsProvider() {
        newsApi = RetrofitProvider.getRetrofit().create(NewsApi.class);
        newsCacheApi = CacheProvider.getRxCache().using(NewsCacheApi.class);
    }

    public void getHomeNews(boolean update, Action1<HomeNewsBean> action1){
        newsCacheApi.getHomeNewsAuto(newsApi.getHomeNews(),new DynamicKey("homeNews"),new EvictDynamicKey(update))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1,Throwable::printStackTrace);
    }
}