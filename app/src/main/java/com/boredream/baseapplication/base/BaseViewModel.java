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

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * VM 作为 Data(repo) 和 UI(Activity/Fragment) 的中间层，处理数据到界面显示的逻辑。
 * 每个页面绑定一个VM
 */
public class BaseViewModel extends ViewModel {

    // live data 可与视图直接绑定，也可以作为事件的回调
    protected final MutableLiveData<Boolean> mDataLoading = new MutableLiveData<>();

    // 一次性事件，不保存状态
    protected final SingleLiveEvent<String> mToastEvent = new SingleLiveEvent<>();

    public MutableLiveData<Boolean> isDataLoading() {
        return mDataLoading;
    }

    public SingleLiveEvent<String> getToastEvent() {
        return mToastEvent;
    }
}
