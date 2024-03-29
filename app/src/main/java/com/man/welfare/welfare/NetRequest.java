package com.man.welfare.welfare;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NetRequest {
    private static NetRequest   netRequest;
    private static OkHttpClient okHttpClient; // OKHttp网络请求
    private        Handler      mHandler;

    private NetRequest() {
        // 初始化okhttp 创建一个OKHttpClient对象
        okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .connectTimeout(500000, TimeUnit.SECONDS)
                .readTimeout(500000, TimeUnit.SECONDS)
                .writeTimeout(500000, TimeUnit.SECONDS);
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式  获取NetRequest实例
     *
     * @return netRequest
     */
    private static NetRequest getInstance() {
        if (netRequest == null) {
            netRequest = new NetRequest();
        }
        return netRequest;
    }

    //-------------对外提供的方法Start--------------------------------

    /**
     * 建立网络框架，获取网络数据，异步get请求（Form）
     *
     * @param url      url
     * @param params   key value
     * @param callBack data
     */
    public static void getFormRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_getFormAsync(url, params, callBack);
    }

    /**
     * 建立网络框架，获取网络数据，异步post请求（Form）
     *
     * @param url      url
     * @param params   key value
     * @param callBack data
     */
    public static void postFormRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_postFormAsync(url, params, callBack);
    }
    /*
     *建立网络框架，获取网络数据，异步post请求（json）
     * @param url      url
     * @param params   key value
     * @param callBack data
     */

    public static void postJsonRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_postJsonAsync(url, params, callBack);
    }
    //-------------对外提供的方法End--------------------------------

    /*
     * 异步get请求（Form），内部实现方法
     *
     * @param url    url
     * @param params key value
     */
    private void inner_getFormAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        if (params == null) {
            params = new HashMap<>();
        }
        // 请求url（baseUrl+参数）
        final String doUrl = urlJoint(url, params);
        // 新建一个请求
        final Request request = new Request.Builder().url(doUrl).build();
        //执行请求获得响应结果
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    /*
     * 异步post请求（Form）,内部实现方法
     *
     * @param url      url
     * @param params   params
     * @param callBack callBack
     */
    private void inner_postFormAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        /**
         * 在这对添加的参数进行遍历
         */
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            /**
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }
            /**
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }
        requestBody = builder.build();
        //结果返回
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String json = response.body().string();
                    deliverDataSuccess(json, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }
    /**
     * post请求传json
     *
     * @param url      url
     * @param callBack 成功或失败回调
     * @param params     params
     */

    private void inner_postJsonAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        // 将map转换成json,需要引入Gson包
        String mapToJson = new Gson().toJson(params);
        final Request request = buildJsonPostRequest(url, mapToJson);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    /*
     * Json_POST请求参数
     *
     * Json_POST请求参数
     * @param url  url
     * @param json json
     * @return requestBody
     */
    private Request buildJsonPostRequest(String url, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 分发失败的时候调用
     *
     * @param request  request
     * @param e        e
     * @param callBack callBack
     */
    private void deliverDataFailure(final Request request, final IOException e, final DataCallBack callBack) {
        /**
         * 在这里使用异步处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(request, e);
                }
            }
        });
    }

    /**
     * 分发成功的时候调用
     *
     * @param result   result
     * @param callBack callBack
     */
    private void deliverDataSuccess(final String result, final DataCallBack callBack) {
        /**
         * 在这里使用异步线程处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.requestSuccess(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 数据回调接口
     */
    public interface DataCallBack {

        void requestSuccess(String result) throws Exception;

        void requestFailure(Request request, IOException e);
    }

    /**
     * 拼接url和请求参数
     *
     * @param url    url
     * @param params key value
     * @return String url
     */
    private static String urlJoint(String url, Map<String, String> params) {
        StringBuilder endUrl = new StringBuilder(url);
        boolean isFirst = true;
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (isFirst && !url.contains("?")) {
                isFirst = false;
                endUrl.append("?");
            } else {
                endUrl.append("&");
            }
            endUrl.append(entry.getKey());
            endUrl.append("=");
            endUrl.append(entry.getValue());
        }
        return endUrl.toString();
    }
}
