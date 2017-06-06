package com.android.znh.worksave.priactice.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * author:  znh
 * time:    2017/6/5.
 * despcraption: 介绍网络访问框架Retrofit
 */
/*

1.compile 'com.squareup.retrofit2:retrofit:3.0'


* */
public class MyRetrofit {
    public String url="https://api.github.com";
    public MyRetrofit(String urlother) {
        if (urlother!=null){
            url=urlother;
        }
    }

    public BlogService createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        BlogService service = retrofit.create(BlogService.class);
        return service;
    }
    public interface BlogService {
//        @GET("blog/{id}")
//        Call<ResponseBody> getBlog(@Path("id") int id);

//        @Headers("Cache-Control: max-age=640000")
//        @GET("widget/list")
//        Call<List<Widget>> widgetList();

        @GET("users/list?sort=desc")
        Call<ResponseBody> getBlog();
    }

}

