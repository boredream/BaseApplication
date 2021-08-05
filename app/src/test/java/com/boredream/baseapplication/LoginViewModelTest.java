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

package com.boredream.baseapplication;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.boredream.baseapplication.base.BaseResponse;
import com.boredream.baseapplication.data.UserInfoRepository;
import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.ui.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

// VM 测试，测试数据和界面的交互逻辑
@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserInfoRepository mRepository;

    private LoginViewModel mViewModel;

    @Before
    public void setupAddEditTaskViewModel() {
        // Get a reference to the class under test
        mViewModel = new LoginViewModel(mRepository);
    }

    @Test
    public void login_empty() {
        mViewModel.login("", "456");
        assertEquals(mViewModel.getToastEvent().getValue(), "用户名不能为空");

        mViewModel.login("123", null);
        assertEquals(mViewModel.getToastEvent().getValue(), "密码不能为空");
    }

    @Test
    public void login_success() {
        UserInfo data = new UserInfo();
        data.setName("05010001");
        data.setPassword("123456q");

        when(mRepository.login(data.getName(), data.getPassword()))
                .thenReturn(Observable.just(new BaseResponse<>(data)));

        mViewModel.login(data.getName(), data.getPassword());

        assertNotNull(mViewModel.getData().getValue());
        assertEquals(mViewModel.getData().getValue().getName(), data.getName());
    }

    @Test
    public void login_error() {
        UserInfo data = new UserInfo();
        data.setName("123");
        data.setPassword("456");

        when(mRepository.login(data.getName(), "234"))
                .thenReturn(Observable.just(new BaseResponse<>(1, "密码错误", null)));

        mViewModel.login(data.getName(), data.getPassword());

        assertNotNull(mViewModel.getData().getValue());
        assertEquals(mViewModel.getData().getValue().getName(), data.getName());
    }
}
