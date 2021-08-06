package com.boredream.baseapplication.data;

import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.net.RxComposer;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

// repo 测试，主要测试接口和基础数据逻辑
@RunWith(MockitoJUnitRunner.class)
public class UserInfoRepositoryTest {

    @Mock
    private UserInfoLocalDataSource mLocalDataSource;

    private UserInfoRepository mRepository;

    @Before
    public void setupRepository() {
        mRepository = UserInfoRepository.getInstance(mLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        UserInfoRepository.destroyInstance();
    }

    @Test
    public void login() {
        // 接口测试
        UserInfo data = mRepository
                .login("05010001", "123456q")
                .compose(RxComposer.defaultResponse())
                .blockingFirst();
        System.out.println(new Gson().toJson(data));
        assertNotNull(data);
    }

}
