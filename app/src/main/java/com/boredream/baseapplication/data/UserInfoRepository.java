/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boredream.baseapplication.data;

import com.boredream.baseapplication.base.BaseResponse;
import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.net.HttpRequest;
import com.boredream.baseapplication.utils.EspressoIdlingResource;
import com.boredream.baseapplication.utils.SpUtils;

import io.reactivex.Observable;

/**
 * repo 处理数据
 */
public class UserInfoRepository {

    private volatile static UserInfoRepository instance;
    private UserInfo mCachedUserInfo;

    private UserInfoRepository() {
    }

    public static UserInfoRepository getInstance() {
        if (instance == null) {
            synchronized (UserInfoRepository.class) {
                if (instance == null) {
                    instance = new UserInfoRepository();
                }
            }
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public Observable<BaseResponse<UserInfo>> getUserInfo() {
        // 缓存
        if (mCachedUserInfo != null) {
            return Observable.just(new BaseResponse<>(mCachedUserInfo));
        }

        // 本地
        UserInfo localUser = SpUtils.get("user", UserInfo.class);
        if (localUser != null) {
            mCachedUserInfo = localUser;
            return Observable.just(new BaseResponse<>(mCachedUserInfo));
        }

        // 远程
        EspressoIdlingResource.increment(); // App is busy until further notice
        return HttpRequest.getApiService().getUserInfo()
                .doOnNext(this::saveUserInfo)
                .doOnComplete(EspressoIdlingResource::decrement); // Set app as idle.
    }

    public Observable<BaseResponse<UserInfo>> login(String username, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(username);
        userInfo.setPassword(password);

        EspressoIdlingResource.increment(); // App is busy until further notice
        return HttpRequest.getApiService().login(userInfo)
                .doOnNext(this::saveUserInfo)
                .doOnComplete(EspressoIdlingResource::decrement); // Set app as idle.
    }

    private void saveUserInfo(BaseResponse<UserInfo> response) {
        if (!response.isSuccess()) return;
        mCachedUserInfo = response.getData();

        // TODO: chunyang 8/5/21 repo 包含 context
        SpUtils.save("user", mCachedUserInfo);
    }

}
