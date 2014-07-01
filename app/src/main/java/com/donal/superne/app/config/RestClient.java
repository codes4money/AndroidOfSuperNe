package com.donal.superne.app.config;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by donal on 14-7-1.
 */
public class RestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        client.setTimeout(60*1000);
        client.setMaxConnections(5);
        if (relativeUrl.contains("http")) {
            return relativeUrl;
        }
        else {
            return Constant.BASE_API + relativeUrl;
        }
    }
}
