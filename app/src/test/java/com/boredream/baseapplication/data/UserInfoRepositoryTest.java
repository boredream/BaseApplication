package com.boredream.baseapplication.data;

import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.net.RxComposer;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;

public class UserInfoRepositoryTest {

    private UserInfoRepository mRepository;

    @Before
    public void setupRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mRepository = UserInfoRepository.getInstance();
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
