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

import com.boredream.baseapplication.base.BaseRepository;
import com.boredream.baseapplication.base.BaseResponse;
import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.net.AppSchedulers;
import com.boredream.baseapplication.net.HttpRequest;

import io.reactivex.Observable;

public class UserInfoRepository extends BaseRepository {

    private volatile static UserInfoRepository instance;
    private final UserInfoLocalDataSource localDataSource;
    private UserInfo cachedUserInfo;

    protected UserInfoRepository(AppSchedulers appSchedulers, UserInfoLocalDataSource localDataSource) {
        super(appSchedulers);
        this.localDataSource = localDataSource;
    }

    public static UserInfoRepository getInstance(AppSchedulers appSchedulers, UserInfoLocalDataSource localDataSource) {
        if (instance == null) {
            synchronized (UserInfoRepository.class) {
                if (instance == null) {
                    instance = new UserInfoRepository(appSchedulers, localDataSource);
                }
            }
        }
        return instance;
    }

    public static UserInfoRepository provideRepository() {
        return UserInfoRepository.getInstance(new AppSchedulers(), new UserInfoLocalDataSource());
    }

    public static void destroyInstance() {
        instance = null;
    }

    public Observable<BaseResponse<UserInfo>> getUserInfo() {
        // 缓存
        if (cachedUserInfo != null) {
            return Observable.just(new BaseResponse<>(cachedUserInfo));
        }

        // 本地
        UserInfo localUser = localDataSource.getUserInfo();
        if (localUser != null) {
            cachedUserInfo = localUser;
            return Observable.just(new BaseResponse<>(cachedUserInfo));
        }

        // 远程
        return HttpRequest.getApiService().getUserInfo()
                .compose(netTransform())
                .doOnNext(this::saveUserInfo);
    }

    public Observable<BaseResponse<UserInfo>> login(String username, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(username);
        userInfo.setPassword(password);

        return HttpRequest.getApiService().login(userInfo)
                .compose(netTransform())
                .doOnNext(this::saveUserInfo);
    }

    private void saveUserInfo(BaseResponse<UserInfo> response) {
        if (!response.isSuccess()) return;
        cachedUserInfo = response.getData();
        localDataSource.saveUserInfo(cachedUserInfo);
    }

}
