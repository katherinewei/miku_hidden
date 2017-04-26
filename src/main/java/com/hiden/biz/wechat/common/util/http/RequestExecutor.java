
package com.hiden.biz.wechat.common.util.http;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public interface RequestExecutor<T, E> {
  T execute(CloseableHttpClient var1, HttpHost var2, String var3, E var4) throws WxErrorException, ClientProtocolException, IOException;

  T execute(CloseableHttpClient var1, HttpHost var2, String var3, E var4, String var5) throws WxErrorException, ClientProtocolException, IOException;
}
