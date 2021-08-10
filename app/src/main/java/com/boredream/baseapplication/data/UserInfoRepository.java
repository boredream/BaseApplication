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

import com.boredream.baseapplication.base.BaseNetRepository;
import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.net.AppSchedulers;
import com.boredream.baseapplication.net.HttpRequest;

import io.reactivex.Observable;

public class UserInfoRepository extends BaseNetRepository {

    private volatile static UserInfoRepository instance;
    private final UserInfoLocalDataSource localDataSource;
    private UserInfo cachedUserInfo;

    // TODO: chunyang 8/9/21 依赖注入框架
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
        // TODO: chunyang 8/10/21 单例模式，所以测试时需要这个
        instance = null;
    }

    public Observable<UserInfo> getUserInfo() {
        // 缓存
        if (cachedUserInfo != null) {
            return Observable.just(cachedUserInfo);
        }

        // 本地
        UserInfo localUser = localDataSource.getUserInfo();
        if (localUser != null) {
            cachedUserInfo = localUser;
            return Observable.just(cachedUserInfo);
        }

        // 远程
        return HttpRequest.getApiService()
                .getUserInfo()
                .compose(baseRespTrans())
                .doOnNext(this::saveData);
    }

    public Observable<Object> sendCode(String phone) {
        // TODO: chunyang 8/9/21 是否应该在user repo里？
        // TODO: chunyang 8/9/21
        return null;
    }

    public Observable<UserInfo> register(UserInfo userInfo) {
        // TODO: chunyang 8/9/21
        return null;
    }

    public Observable<UserInfo> login(UserInfo userInfo) {
        return HttpRequest.getApiService()
                .login(userInfo)
                .compose(baseRespTrans())
                .doOnNext(this::saveData);
    }

    public Observable<Object> resetPassword(UserInfo userInfo) {
        // TODO: chunyang 8/9/21
        return null;
    }

    private void saveData(UserInfo data) {
        cachedUserInfo = data;
        localDataSource.saveUserInfo(cachedUserInfo);
    }

}
