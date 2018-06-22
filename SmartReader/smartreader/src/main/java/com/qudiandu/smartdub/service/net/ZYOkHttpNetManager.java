package com.qudiandu.smartdub.service.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.qudiandu.smartdub.utils.ZYLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ZY on 17/4/13.
 */

public class ZYOkHttpNetManager {

    private static ZYOkHttpNetManager instance;

    private OkHttpClient mOkHttpClient;

    private boolean isCancle;

    private ZYOkHttpNetManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .build();
    }

    public static ZYOkHttpNetManager getInstance() {
        if (instance == null) {
            instance = new ZYOkHttpNetManager();
        }
        return instance;
    }

    public void reset() {
        isCancle = false;
    }

    public void requestGet(final Class classBean, String url, final OkHttpNetListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }
        reset();
        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onFailure: " + e.getMessage());
                    if (listener != null && !isCancle) {
                        listener.onFailure("网络异常,请重新尝试");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (listener != null && !isCancle) {
                        try {
                            listener.onSuccess(new Gson().fromJson(response.body().toString(), classBean));
                        } catch (Exception e) {
                            ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onResponse-error: " + e.getMessage());
                            listener.onFailure("网络异常,请重新尝试");
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null && !isCancle) {
                listener.onFailure("网络异常,请重新尝试!");
            }
        }
    }

    public void downloadFile(String url, final String filePath, final OkHttpNetListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }
        reset();
        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onFailure: " + e.getMessage());
                    if (listener != null && !isCancle) {
                        listener.onFailure("网络异常,请重新尝试");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (listener != null && !isCancle) {
                        try {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            if (response.isSuccessful()) {
                                inputStream = response.body().byteStream();
                                outputStream = new FileOutputStream(new File(filePath));
                                byte data[] = new byte[1024];
                                int count;
                                while ((count = inputStream.read(data)) != -1) {
                                    if (isCancle) {
                                        break;
                                    }
                                    outputStream.write(data, 0, count);
                                }
                                outputStream.flush();
                                outputStream.close();
                                inputStream.close();
                                if (listener != null && !isCancle) {
                                    listener.onSuccess(filePath);
                                }
                            } else {
                                if (listener != null && !isCancle) {
                                    listener.onFailure("网络异常,请重新尝试");
                                }
                            }
                        } catch (Exception e) {
                            ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onResponse-error: " + e.getMessage());
                            if (listener != null && !isCancle) {
                                listener.onFailure("网络异常,请重新尝试");
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null && !isCancle) {
                listener.onFailure("网络异常,请重新尝试!");
            }
        }
    }

    public void requestPost(final Class classBean, String url, Map<String, String> params, final OkHttpNetListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }
        reset();
        try {

            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    bodyBuilder.add(entry.getKey(), entry.getValue());
                }
            }

            Request request = new Request.Builder()
                    .url(url)
                    .post(bodyBuilder.build())
                    .build();

            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onFailure: " + e.getMessage());
                    if (listener != null && !isCancle) {
                        listener.onFailure("网络异常,请重新尝试");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (listener != null && !isCancle) {
                        try {
                            String result = response.body().string();
                            listener.onSuccess(new Gson().fromJson(result, classBean));
                        } catch (Exception e) {
                            ZYLog.e(ZYOkHttpNetManager.class.getSimpleName(), "onResponse-error: " + e.getMessage());
                            listener.onFailure("网络异常,请重新尝试");
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null && !isCancle) {
                listener.onFailure("网络异常,请重新尝试!");
            }
        }
    }

    public void cancle() {
        isCancle = true;
        mOkHttpClient.dispatcher().cancelAll();
    }

    public interface OkHttpNetListener<T> {
        void onFailure(String message);

        void onSuccess(T response);
    }

}
