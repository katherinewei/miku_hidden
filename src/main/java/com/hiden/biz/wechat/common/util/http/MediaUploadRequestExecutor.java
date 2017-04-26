
package com.hiden.biz.wechat.common.util.http;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.model.response.WxMediaUploadResult;
import com.hiden.biz.wechat.common.model.response.error.WxError;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;

public class MediaUploadRequestExecutor implements RequestExecutor<WxMediaUploadResult, File> {
    public MediaUploadRequestExecutor() {
    }

    public WxMediaUploadResult execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, File file) throws WxErrorException, ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpPost.setConfig(response);
        }

        if(file != null) {
            HttpEntity response1 = MultipartEntityBuilder.create().addBinaryBody("media", file).build();
            httpPost.setEntity(response1);
            httpPost.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.toString());
        }

        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        String responseContent = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response2);
        WxError error = WxError.fromJson(responseContent);
        if(error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        } else {
            return WxMediaUploadResult.fromJson(responseContent);
        }
    }

    public WxMediaUploadResult execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, File file, String mpTag) throws WxErrorException, ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpPost.setConfig(response);
        }

        if(file != null) {
            HttpEntity response1 = MultipartEntityBuilder.create().addBinaryBody("media", file).build();
            httpPost.setEntity(response1);
            httpPost.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.toString());
        }

        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        String responseContent = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response2);
        WxError error = WxError.fromJson(responseContent);
        if(error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        } else {
            return WxMediaUploadResult.fromJson(responseContent);
        }
    }
}
