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

package com.boredream.baseapplication.ui;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.StringUtils;
import com.boredream.baseapplication.base.BaseViewModel;
import com.boredream.baseapplication.data.UserInfoRepository;
import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.net.RxComposer;
import com.boredream.baseapplication.net.SimpleObserver;

public class LoginViewModel extends BaseViewModel {

    private final UserInfoRepository mRepository;

    private final MutableLiveData<UserInfo> mData = new MutableLiveData<>();

    public MutableLiveData<UserInfo> getData() {
        return mData;
    }

    public LoginViewModel(UserInfoRepository repository) {
        mRepository = repository;
    }

    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            mToastEvent.setValue("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            mToastEvent.setValue("密码不能为空");
            return;
        }

        mRepository.login(username, password)
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleObserver<UserInfo>() {
                    @Override
                    public void onNext(UserInfo userInfo) {
                        mData.setValue(userInfo);
                    }
                });
    }

}
