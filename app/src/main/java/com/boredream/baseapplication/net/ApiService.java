package com.boredream.baseapplication.net;

import com.boredream.baseapplication.base.BaseResponse;
import com.boredream.baseapplication.data.entity.BeanInfo;
import com.boredream.baseapplication.data.entity.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("user/login")
    Observable<BaseResponse<UserInfo>> login(
            @Body UserInfo userInfo);

    @GET("xx/xx")
    Observable<BaseResponse<UserInfo>> getUserInfo();

    @GET("xx/xx")
    Observable<BaseResponse<BeanInfo>> getInfo(
            @Query("name") String name);

    @POST("xx/xx")
    Observable<BaseResponse<String>> addInfo(
            @Body BeanInfo request);

}
