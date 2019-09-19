package com.tanmengen.en.tanmengenqi;

import com.tanmengen.en.tanmengenqi.beans.BannerBean;
import com.tanmengen.en.tanmengenqi.beans.FuliBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by en on 2019/9/3.
 */

public interface ApiService {
    String getbannerurl="https://www.wanandroid.com/";
    @GET("banner/json")
    Observable<BannerBean> getBanner();

    String getFuliurl="https://www.wanandroid.com/";
    @GET("article/list/{page}/json")
    Observable<FuliBean> getfuli(@Path("page") int page);

}
