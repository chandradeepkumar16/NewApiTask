package com.example.news;

import com.example.news.ModelClasses.MainResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsInterface
{
    @GET("/v2/top-headlines?country=in&apiKey=29ace5361f5244f9a7e8afeb7f7a8594")
    Call<MainResponse> getNewsData(@Query("general") String allnews);
}
