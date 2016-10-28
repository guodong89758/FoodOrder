package com.foodorder.server;

import android.text.TextUtils;

import com.foodorder.R;
import com.foodorder.contant.AppKey;
import com.foodorder.log.DLOG;
import com.foodorder.runtime.RT;
import com.foodorder.server.callback.ResponseCallback;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.lzy.okhttputils.request.BaseRequest;

import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Response;


public class NetworkInterface {

    private static volatile NetworkInterface instance = null;

    private NetworkInterface() {

    }

    public static NetworkInterface ins() {
        NetworkInterface mInstance = instance;
        if (mInstance == null) {
            synchronized (NetworkInterface.class) {
                mInstance = instance;
                if (mInstance == null) {
                    mInstance = new NetworkInterface();
                    instance = mInstance;
                }
            }
        }
        return mInstance;
    }

    /**
     * @param apiTag
     * @param request  接口名称
     * @param params   参数列表
     * @param callback 回调接口
     */
    public void connected(final HttpMethod method, final String request, final String apiTag, final HttpParams params, final ResponseCallback callback) {
        final String url = getRequsetUrl(request);
        if (!url.contains("http://")) {
            DLOG.e(AppKey.HTTP_TAG, "Bad request url ==" + url);
            return;
        }
        if (method == HttpMethod.POST) {
            connectedByPost(url, apiTag, params, callback);
        } else if (method == HttpMethod.GET) {
            connectedByGet(url, apiTag, params, callback);
        }
    }

    /**
     * @param requestUrl 地址
     * @param params     参数列表
     * @param apiTag
     * @param callback   回调接口
     */

    private void connectedByPost(final String requestUrl, final String apiTag, final HttpParams params, final ResponseCallback callback) {
        {
            if (RT.DEBUG) {
                DLOG.d(AppKey.HTTP_TAG, requestUrl);
            }
            OkHttpUtils.post(requestUrl).params(params).tag(apiTag).execute(new StringCallback() {
                @Override
                public void onBefore(BaseRequest request) {
                    super.onBefore(request);
                    callback.onPreRequest();
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (RT.DEBUG && !TextUtils.isEmpty(s)) {
                        DLOG.json(AppKey.HTTP_TAG, s);
                    }
                    callback.onResponse(s.getBytes(), 0, "", 0, false);
                }

                @Override
                public void onCacheSuccess(String s, Call call) {
                    super.onCacheSuccess(s, call);
                    if (RT.DEBUG && !TextUtils.isEmpty(s)) {
                        DLOG.json(AppKey.HTTP_CACHE_TAG, s);
                    }
                    callback.onResponse(s.getBytes(), 0, "", 0, true);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    if (e instanceof UnknownHostException) {
                        callback.onResponse(null, -1, RT.getString(R.string.error_http_request), 0, false);
                    } else {
                        callback.onResponse(null, -1, "", 0, false);
                    }
                }

                @Override
                public void onCacheError(Call call, Exception e) {
                    super.onCacheError(call, e);
                    if (e instanceof UnknownHostException) {
                        callback.onResponse(null, -1, RT.getString(R.string.error_http_request), 0, true);
                    } else {
                        callback.onResponse(null, -1, "", 0, true);
                    }
                }

            });
        }
    }

    /**
     * @param requestUrl 地址
     * @param params     参数列表
     * @param apiTag
     * @param callback   回调接口
     */
    public void connectedByGet(final String requestUrl, String apiTag, HttpParams params, final ResponseCallback callback) {
        if (RT.DEBUG) {
            DLOG.d(AppKey.HTTP_TAG, requestUrl);
        }
        OkHttpUtils.get(requestUrl).params(params).tag(apiTag).execute(new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                callback.onPreRequest();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (RT.DEBUG && !TextUtils.isEmpty(s)) {
                    DLOG.json(AppKey.HTTP_TAG, s);
                }
                callback.onResponse(s.getBytes(), 0, "", 0, false);
            }

            @Override
            public void onCacheSuccess(String s, Call call) {
                super.onCacheSuccess(s, call);
                if (RT.DEBUG && !TextUtils.isEmpty(s)) {
                    DLOG.json(AppKey.HTTP_CACHE_TAG, s);
                }
                callback.onResponse(s.getBytes(), 0, "", 0, true);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (e instanceof UnknownHostException) {
                    callback.onResponse(null, -1, RT.getString(R.string.error_http_request), 0, false);
                } else {
                    callback.onResponse(null, -1, "", 0, false);
                }
            }

            @Override
            public void onCacheError(Call call, Exception e) {
                super.onCacheError(call, e);
                if (e instanceof UnknownHostException) {
                    callback.onResponse(null, -1, RT.getString(R.string.error_http_request), 0, true);
                } else {
                    callback.onResponse(null, -1, "", 0, true);
                }
            }

        });
    }

    /**
     * 根据API返回完整的请求地址
     *
     * @param request
     * @return
     */

    public String getRequsetUrl(String request) {

        return "http://" + ServerManager.SERVER_DOMAIN + "/" + request;
    }
}
