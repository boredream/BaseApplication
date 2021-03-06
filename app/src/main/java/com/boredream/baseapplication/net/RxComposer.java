package com.boredream.baseapplication.net;

import com.boredream.baseapplication.base.BaseViewModel;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;

/**
 * Rx組件。一般情况下直接使用组合类compose，如果有特殊需要自行组装基础compose
 */
public class RxComposer {

    ////////////////////////////// 常用组合compose //////////////////////////////

    // TODO: chunyang 8/5/21 和vm解耦

    /**
     * 常规显示进度框样式
     */
    public static <T> ObservableTransformer<T, T> commonProgress(BaseViewModel vm) {
        return upstream -> upstream
                .compose(defaultFailed(vm))
                .compose(handleProgress(vm));
    }

    ////////////////////////////// 基础compose //////////////////////////////

    /**
     * error统一处理，自动提示Toast
     */
    public static <T> ObservableTransformer<T, T> defaultFailed(BaseViewModel vm) {
        return upstream -> upstream
                .doOnError(throwable -> {
                    vm.getToastEvent().setValue(ErrorConstants.parseHttpErrorInfo(throwable));
                });
    }

    /**
     * 进度框统一处理，发送请求时自动 showProgress，请求成功/失败时自动 dismissProgress
     */
    public static <T> ObservableTransformer<T, T> handleProgress(BaseViewModel vm) {
        return upstream -> upstream
                .doOnSubscribe(disposable -> vm.isDataLoading().setValue(true)) // 接口发出
                .doOnComplete((Action) () -> vm.isDataLoading().setValue(false)); // 接口完成
    }

    // TODO: chunyang 8/5/21
//    /**
//     * 刷新控件统一处理，发送请求时自动 showRefresh，请求成功/失败时自动 dismissRefresh
//     *
//     * @param showContentLoading 显示列表中的loading样式，需要setEmptyAdapterWrap
//     * @param loadMore    true-加载更多, false-下拉刷新
//     */
//    public static <T> ObservableTransformer<T, T> handleRefresh(final BaseView view, boolean showContentLoading, boolean loadMore) {
//        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> view.showRefresh(showContentLoading, loadMore))
//                .doOnError(throwable -> view.dismissRefresh(showContentLoading, loadMore))
//                .doOnNext((Consumer<T>) t -> view.dismissRefresh(showContentLoading, loadMore));
//    }
}
