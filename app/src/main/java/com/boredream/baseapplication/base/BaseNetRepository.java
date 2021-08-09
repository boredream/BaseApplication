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

package com.boredream.baseapplication.base;

import com.boredream.baseapplication.net.ApiException;
import com.boredream.baseapplication.net.AppSchedulers;
import com.boredream.baseapplication.net.EspressoIdlingResource;

import io.reactivex.ObservableTransformer;

/**
 * repo 处理网络数据
 * <p>
 * 1. 远程、本地等方式获取数据
 * 2. 做最基本的数据处理，如判断code
 * 3. 保存至内存、本地等位置
 * </p>
 */
public abstract class BaseNetRepository {

    protected final AppSchedulers appSchedulers;

    public BaseNetRepository(AppSchedulers appSchedulers) {
        this.appSchedulers = appSchedulers;
    }

    /**
     * 处理网络相关
     */
    protected <T> ObservableTransformer<T, T> netTrans() {
        return upstream -> upstream
                .doOnSubscribe(disposable -> EspressoIdlingResource.increment())
                .subscribeOn(appSchedulers.getNetThread())
                .observeOn(appSchedulers.getMainThread())
                .doOnComplete(EspressoIdlingResource::decrement);
    }

    /**
     * 处理网络相关，基于数据结构BaseResponse
     */
    protected <T> ObservableTransformer<BaseResponse<T>, T> baseRespTrans() {
        return upstream -> upstream
                .map(response -> {
                    // 至此网络请求正常，但可能自定义的数据里有code=xxx，代表着业务类错误，在此处理
                    if (!response.isSuccess()) {
                        throw new ApiException(response);
                    }
                    return response.getData();  // TODO: data null
                })
                .compose(netTrans());
    }

}
