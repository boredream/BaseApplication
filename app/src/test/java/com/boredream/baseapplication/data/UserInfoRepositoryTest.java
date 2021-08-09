package com.boredream.baseapplication.data;

import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.net.AppSchedulers;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;

// repo 测试，主要测试接口和基础数据逻辑
@RunWith(MockitoJUnitRunner.class)
public class UserInfoRepositoryTest {

    @Mock
    private UserInfoLocalDataSource localDataSource;

    private UserInfoRepository repository;

    @Before
    public void setupRepository() {
        Scheduler scheduler = Schedulers.trampoline();
        AppSchedulers appSchedulers = new AppSchedulers(scheduler, scheduler, scheduler);
        repository = UserInfoRepository.getInstance(appSchedulers, localDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        UserInfoRepository.destroyInstance();
    }

    @Test
    public void login() {

        // TODO: chunyang 8/6/21   RxJavaPlugins.setIoSchedulerHandler { t -> Schedulers.trampoline() } ??

        // 接口测试
        UserInfo userInfo = new UserInfo();
        userInfo.setName("05010001");
        userInfo.setPassword("123456q");

        UserInfo data = repository
                .login(userInfo)
                .blockingFirst();
        System.out.println(new Gson().toJson(data));
        assertNotNull(data);
    }

}
