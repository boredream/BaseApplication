package com.boredream.baseapplication.net;

import android.os.NetworkOnMainThreadException;

import androidx.annotation.NonNull;

import com.boredream.baseapplication.base.BaseResponse;
import com.boredream.baseapplication.base.BaseView;
import com.google.gson.JsonParseException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ErrorConstants {

    // TODO 按需自定义添加要特殊处理的业务错误码

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 版本过低，强制用户升级
     */
    public static final int LOW_VERSION = 40003;

    /**
     * token过期，重新登录，跳到登录页面
     */
    public static final int TOKEN_EXPIRED = 40004;

    /**
     * 账号被踢，重新登录，跳到登录页面
     */
    public static final int LOGIN_KICK_ASS = 40005;

    /**
     * token错误
     */
    public static final int TOKEN_ERROR = 40006;

    public static boolean compareApiError(Throwable throwable, int targetCode) {
        boolean result = false;
        if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            BaseResponse<?> body = apiException.getBody();
            if (body != null) {
                result = body.getCode() == targetCode;
            }
        }
        return result;
    }

    public static boolean compareApiError(Throwable throwable, @NonNull String targetMsg) {
        boolean result = false;
        if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            BaseResponse<?> body = apiException.getBody();
            if (body != null && body.getMsg() != null) {
                result = body.getMsg().equals(targetMsg);
            }
        }
        return result;
    }

    /**
     * 解析服务器错误信息
     */
    public static String parseHttpErrorInfo(Throwable throwable) {
        return parseHttpErrorInfo(null, throwable);
    }

    /**
     * 解析服务器错误信息
     */
    public static String parseHttpErrorInfo(BaseView view, Throwable throwable) {
        String errorInfo = throwable.getMessage();

        // TODO: chunyang 2/23/21 自己加网络工具类
//        if (!NetUtils.isConnected(AppKeeper.getApp())) {
//            errorInfo = "网络未连接";
//        } else
            if (throwable instanceof HttpException) {
            // 如果是Retrofit的Http错误,则转换类型,获取信息
            HttpException exception = (HttpException) throwable;
            errorInfo = "服务器错误" + exception.code();
        } else if (throwable instanceof NetworkOnMainThreadException) {
            errorInfo = "网络请求不能在主线程";
        } else if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            // 优先使用服务端返回错误
            BaseResponse<?> errorBody = apiException.getBody();
            errorInfo = errorBody.getMsg();

            // TODO: chunyang 2/23/21 各种自定义处理
            switch (errorBody.getCode()) {
                case LOW_VERSION:
                    break;
                case TOKEN_EXPIRED:
                case TOKEN_ERROR:
//                    UserInfoKeeper.logout();
//                    Context context401 = AppKeeper.getApp();
//                    Intent intent401 = new Intent(context401, AppKeeper.getLoginClass());
//                    intent401.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent401.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    context401.startActivity(intent401);
                    break;
            }
        } else if (throwable instanceof ConnectException) {
            errorInfo = "无法连接服务器";
        } else if (throwable instanceof UnknownHostException) {
            errorInfo = "服务器连接失败";
        } else if (throwable instanceof SocketTimeoutException) {
            errorInfo = "服务器连接超时";
        } else if ("The mapper function returned a null value.".equals(throwable.getMessage())
                || throwable instanceof JsonParseException
                || throwable instanceof EOFException) {
            errorInfo = "数据解析错误 " + throwable.getMessage();
        }

        // 缺省处理
        if (errorInfo == null || errorInfo.length() == 0) {
            errorInfo = "未知错误 " + throwable.getMessage();
        }

        return errorInfo;
    }

}
