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

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    private UserInfoRepository repository;
    public MutableLiveData<UserInfo> data = new MutableLiveData<>();
    public MutableLiveData<Boolean> sendCodeBtnEnable = new MutableLiveData<>(true);
    public MutableLiveData<String> sendCodeBtnText = new MutableLiveData<>("发送验证码");

    private Disposable countDownDisposable;

    public LoginViewModel(UserInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        countDownDisposable.dispose();
    }

    public void sendCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            toastEvent.setValue("手机号不能为空");
            return;
        }

        repository.sendCode(phone)
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        startCountDown();
                    }
                });
    }

    private void startCountDown() {
        int totalSecond = 60;
        countDownDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .take(totalSecond)
                .map(aLong -> totalSecond - aLong - 1) // totalSecond-1, -2, ... 1, stop
                .doOnSubscribe(disposable -> {
                    sendCodeBtnEnable.postValue(false);
                    sendCodeBtnText.postValue(String.format(Locale.getDefault(), "%ds", totalSecond));
                })
                .subscribeOn(Schedulers.io()) // TODO: chunyang 8/9/21 这里是否应该出现线程处理？
                .subscribe(aLong -> sendCodeBtnText.postValue(String.format(Locale.getDefault(), "%ds", aLong)),
                        throwable -> {/**/},
                        this::stopCountDown);
    }

    private void stopCountDown() {
        sendCodeBtnEnable.postValue(true);
        sendCodeBtnText.postValue("发送验证码");
    }

    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            toastEvent.setValue("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            toastEvent.setValue("密码不能为空");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setName(username);
        userInfo.setPassword(password);

        repository.login(userInfo)
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleObserver<UserInfo>() {
                    @Override
                    public void onNext(UserInfo userInfo) {
                        data.setValue(userInfo);
                    }
                });
    }

}
