package com.hiden.biz.wechat.mp.util.http;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.model.response.error.WxError;
import com.hiden.biz.wechat.common.util.fileUtil.FileUtils;
import com.hiden.biz.wechat.common.util.http.InputStreamResponseHandler;
import com.hiden.biz.wechat.common.util.http.RequestExecutor;
import com.hiden.biz.wechat.common.util.http.Utf8ResponseHandler;
import com.hiden.biz.wechat.mp.bean.result.WxMpQrCodeTicket;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;

public class QrCodeRequestExecutor implements RequestExecutor<File, WxMpQrCodeTicket> {
    public QrCodeRequestExecutor() {
    }

    public File execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, WxMpQrCodeTicket ticket) throws WxErrorException, ClientProtocolException, IOException {
        if(ticket != null) {
            if(uri.indexOf(63) == -1) {
                uri = uri + '?';
            }

            uri = uri + (uri.endsWith("?")?"ticket=" + URLEncoder.encode(ticket.getTicket(), "UTF-8"):"&ticket=" + URLEncoder.encode(ticket.getTicket(), "UTF-8"));
        }

        HttpGet httpGet = new HttpGet(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpGet.setConfig(response);
        }

        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        Header[] contentTypeHeader = response1.getHeaders("Content-Type");
        if(contentTypeHeader != null && contentTypeHeader.length > 0 && ContentType.TEXT_PLAIN.getMimeType().equals(contentTypeHeader[0].getValue())) {
            String inputStream1 = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response1);
            throw new WxErrorException(WxError.fromJson(inputStream1));
        } else {
            InputStream inputStream = (InputStream)InputStreamResponseHandler.INSTANCE.handleResponse(response1);
            File localFile = FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), "jpg");
            return localFile;
        }
    }

    public File execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, WxMpQrCodeTicket ticket, String mpTag) throws WxErrorException, ClientProtocolException, IOException {
        if(ticket != null) {
            if(uri.indexOf(63) == -1) {
                uri = uri + '?';
            }

            uri = uri + (uri.endsWith("?")?"ticket=" + URLEncoder.encode(ticket.getTicket(), "UTF-8"):"&ticket=" + URLEncoder.encode(ticket.getTicket(), "UTF-8"));
        }

        HttpGet httpGet = new HttpGet(uri);
        if(httpProxy != null) {
            RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
            httpGet.setConfig(response);
        }

        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        Header[] contentTypeHeader = response1.getHeaders("Content-Type");
        if(contentTypeHeader != null && contentTypeHeader.length > 0 && ContentType.TEXT_PLAIN.getMimeType().equals(contentTypeHeader[0].getValue())) {
            String inputStream1 = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response1);
            throw new WxErrorException(WxError.fromJson(inputStream1));
        } else {
            InputStream inputStream = (InputStream)InputStreamResponseHandler.INSTANCE.handleResponse(response1);
            File localFile = FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), "jpg");
            return localFile;
        }
    }
}
