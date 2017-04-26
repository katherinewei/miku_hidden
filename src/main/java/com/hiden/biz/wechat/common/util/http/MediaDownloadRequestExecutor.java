
package com.hiden.biz.wechat.common.util.http;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.model.response.error.WxError;
import com.hiden.biz.wechat.common.util.StringUtils;
import com.hiden.biz.wechat.common.util.fileUtil.FileUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaDownloadRequestExecutor implements RequestExecutor<File, String> {
    public MediaDownloadRequestExecutor() {
    }

    public File execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String queryParam) throws WxErrorException, ClientProtocolException, IOException {
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
        Header[] contentTypeHeader = response1.getHeaders("Content-Type");
        if(contentTypeHeader != null && contentTypeHeader.length > 0 && ContentType.TEXT_PLAIN.getMimeType().equals(contentTypeHeader[0].getValue())) {
            String inputStream1 = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response1);
            throw new WxErrorException(WxError.fromJson(inputStream1));
        } else {
            InputStream inputStream = (InputStream)InputStreamResponseHandler.INSTANCE.handleResponse(response1);
            String fileName = this.getFileName(response1);
            if(StringUtils.isBlank(fileName)) {
                return null;
            } else {
                String[] name_ext = fileName.split("\\.");
                File localFile = FileUtils.createTmpFile(inputStream, name_ext[0], name_ext[1]);
                return localFile;
            }
        }
    }

    public File execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String queryParam, String mpTag) throws WxErrorException, ClientProtocolException, IOException {
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
        Header[] contentTypeHeader = response1.getHeaders("Content-Type");
        if(contentTypeHeader != null && contentTypeHeader.length > 0 && ContentType.TEXT_PLAIN.getMimeType().equals(contentTypeHeader[0].getValue())) {
            String inputStream1 = (String)Utf8ResponseHandler.INSTANCE.handleResponse(response1);
            throw new WxErrorException(WxError.fromJson(inputStream1));
        } else {
            InputStream inputStream = (InputStream)InputStreamResponseHandler.INSTANCE.handleResponse(response1);
            String fileName = this.getFileName(response1);
            if(StringUtils.isBlank(fileName)) {
                return null;
            } else {
                String[] name_ext = fileName.split("\\.");
                File localFile = FileUtils.createTmpFile(inputStream, name_ext[0], name_ext[1]);
                return localFile;
            }
        }
    }

    protected String getFileName(CloseableHttpResponse response) {
        Header[] contentDispositionHeader = response.getHeaders("Content-disposition");
        Pattern p = Pattern.compile(".*filename=\"(.*)\"");
        Matcher m = p.matcher(contentDispositionHeader[0].getValue());
        m.matches();
        String fileName = m.group(1);
        return fileName;
    }
}
