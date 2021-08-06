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

import com.boredream.baseapplication.data.entity.UserInfo;
import com.boredream.baseapplication.utils.SpUtils;

public class UserInfoLocalDataSource {

    private static volatile UserInfoLocalDataSource instance = null;

    public static UserInfoLocalDataSource getInstance() {
        if (instance == null) {
            synchronized (UserInfoLocalDataSource.class) {
                if (instance == null) {
                    instance = new UserInfoLocalDataSource();
                }
            }
        }
        return instance;
    }

    private UserInfoLocalDataSource() {
        // private
    }


    public void saveUserInfo(UserInfo userInfo) {
        SpUtils.save("user", userInfo);
    }

    public UserInfo getUserInfo() {
        return SpUtils.get("user", UserInfo.class);
    }
}
