
package com.hiden.biz.wechat.common.util.http;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.model.response.error.WxError;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class SimplePostRequestExecutor implements RequestExecutor<String, String> {
    public SimplePostRequestExecutor() {
    }

    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String postEntity) throws WxErrorException, ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpPost.setConfig(response);
        }

        if(postEntity != null) {
            StringEntity response1 = new StringEntity(postEntity, Consts.UTF_8);
            httpPost.setEntity(response1);
        }

        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        String responseContent = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response2);
        WxError error = WxError.fromJson(responseContent);
        if(error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        } else {
            return responseContent;
        }
    }

    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String postEntity, String mpTag) throws WxErrorException, ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpPost.setConfig(response);
        }

        if(postEntity != null) {
            StringEntity response1 = new StringEntity(postEntity, Consts.UTF_8);
            httpPost.setEntity(response1);
        }

        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        String responseContent = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response2);
        WxError error = WxError.fromJson(responseContent);
        if(error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        } else {
            return responseContent;
        }
    }
}
