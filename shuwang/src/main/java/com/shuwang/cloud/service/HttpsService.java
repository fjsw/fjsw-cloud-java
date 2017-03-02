package com.shuwang.cloud.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

/**
 * Created by w景洋
 * Time 2016/12/16.
 */
public class HttpsService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
	private Gson gson = new Gson();

    //表示请求器是否已经做了初始化工作
    private boolean hasInit = false;
    //连接超时时间，默认10秒
    private int socketTimeout = 10000;
    //传输超时时间，默认30秒
    private int connectTimeout = 30000;
    //请求器的配置
    private RequestConfig requestConfig;
    //HTTP请求器
    private CloseableHttpClient httpClient;


    //初始化请求器
    private void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        hasInit = true;
    }
    
    public String jsonPost(String url, Map<String, Object> params) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
    	String jsonObj = gson.toJson(params);
        log.info("jsonPost() {}", jsonObj);
        if (!hasInit) {
            init();
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        StringEntity postEntity = new StringEntity(jsonObj, "UTF-8");
        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.setEntity(postEntity);
        //设置请求器的配置
        httpPost.setConfig(requestConfig);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            log.info("code {}",response.getStatusLine().getStatusCode());
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ConnectionPoolTimeoutException e) {
            log.error("http get throw ConnectionPoolTimeoutException(wait time out)");

        } catch (ConnectTimeoutException e) {
            log.error("http get throw ConnectTimeoutException");

        } catch (SocketTimeoutException e) {
            log.error("http get throw SocketTimeoutException");

        } catch (Exception e) {
            log.error("http get throw Exception");

        } finally {
            httpPost.abort();
        }
        return result;
    }

    //提供接入
    public String formPost(String url, Map<String, Object> params) throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    	String formObj = queryString(params);
        log.info("formPost() {}", formObj);
        if (!hasInit) {
            init();
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        StringEntity postEntity = new StringEntity(formObj, "UTF-8");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(postEntity);
        //设置请求器的配置
        httpPost.setConfig(requestConfig);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            log.info("code {}",response.getStatusLine().getStatusCode());
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ConnectionPoolTimeoutException e) {
            log.error("http get throw ConnectionPoolTimeoutException(wait time out)");

        } catch (ConnectTimeoutException e) {
            log.error("http get throw ConnectTimeoutException");

        } catch (SocketTimeoutException e) {
            log.error("http get throw SocketTimeoutException");

        } catch (Exception e) {
            log.error("http get throw Exception");

        } finally {
            httpPost.abort();
        }
        return result;
    }

    private String queryString(Map<String, Object> params){
        StringBuilder query = new StringBuilder();
        //
        Set<String> keyset = params.keySet();
        for (String key : keyset) {
            String value = params.get(key).toString();
            if (!value.isEmpty()) {
                try {
                    query.append(key).append("=")
                            .append(URLEncoder.encode(value, "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        String form_data = query.toString();
        return form_data;
    }
}
