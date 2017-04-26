
package com.hiden.biz.wechat.common.util.http;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.model.response.error.WxError;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class SimpleGetRequestExecutor implements RequestExecutor<String, String> {
    public SimpleGetRequestExecutor() {
    }

    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String queryParam) throws WxErrorException, ClientProtocolException, IOException {
        if(queryParam != null) {
            if(uri.indexOf(63) == -1) {
                uri = uri + '?';
            }

            uri = uri + (uri.endsWith("?")?queryParam:'&' + queryParam);
        }

        HttpGet httpGet = new HttpGet(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpGet.setConfig(response);
        }

        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        String responseContent = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response1);
        WxError error = WxError.fromJson(responseContent);
        if(error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        } else {
            return responseContent;
        }
    }

    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String queryParam, String mpTag) throws WxErrorException, ClientProtocolException, IOException {
        if(queryParam != null) {
            if(uri.indexOf(63) == -1) {
                uri = uri + '?';
            }

            uri = uri + (uri.endsWith("?")?queryParam:'&' + queryParam);
        }

        HttpGet httpGet = new HttpGet(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpGet.setConfig(response);
        }

        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        String responseContent = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response1);
        WxError error = WxError.fromJson(responseContent);
        if(error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        } else {
            return responseContent;
        }
    }
}
