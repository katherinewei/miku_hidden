
package com.hiden.biz.wechat.common.util.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Utf8ResponseHandler implements ResponseHandler<String> {
    public static final ResponseHandler<String> INSTANCE = new Utf8ResponseHandler();

    public Utf8ResponseHandler() {
    }

    public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if(statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        } else {
            return entity == null?null:EntityUtils.toString(entity, Consts.UTF_8);
        }
    }
}
