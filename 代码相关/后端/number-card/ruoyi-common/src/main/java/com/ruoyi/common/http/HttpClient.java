package com.ruoyi.common.http;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class HttpClient {
    public static final String TAG = "HttpClient";

    public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    public static final int HTTP_CODE_SUCCESS = 200;

    private HttpProperties httpProperties;
    private OkHttpClient httpClient;


    public HttpClient(HttpProperties properties) {
        httpProperties = properties;
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(properties.getMaxHostConnection());
        dispatcher.setMaxRequests(properties.getMaxConnection());
        X509TrustManager trustManager = x509TrustManager();
        SSLSocketFactory sslSocketFactory = sslSocketFactory(trustManager);
        if (httpProperties.getIsEnabled()) {
            httpClient = new OkHttpClient
                    .Builder()
                    .dispatcher(dispatcher)
                    .retryOnConnectionFailure(properties.isResetConnection())
                    .connectTimeout(properties.getTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(properties.getTimeout(), TimeUnit.SECONDS)
                    .callTimeout(properties.getTimeout(), TimeUnit.MILLISECONDS)
                    .connectionPool(new ConnectionPool(properties.getMaxIdleConnections(), properties.getKeepAliveDurationMill(), TimeUnit.MILLISECONDS))
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            //跳过验证
                            return true;
                        }
                    })
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("39.107.226.118", 13128)))
                    //代理的鉴权账号密码
                    .proxyAuthenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            //设置代理服务器账号密码
                            String credential = Credentials.basic("wjkproxy", "Wjk12345");
                            return response.request().newBuilder()
                                    .header("Proxy-Authorization", credential)
                                    .build();
                        }
                    })
                    .build();
        } else {
            httpClient = new OkHttpClient
                    .Builder()
                    .dispatcher(dispatcher)
                    .retryOnConnectionFailure(properties.isResetConnection())
                    .connectTimeout(properties.getTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(properties.getTimeout(), TimeUnit.SECONDS)
                    .callTimeout(properties.getTimeout(), TimeUnit.MILLISECONDS)
                    .connectionPool(new ConnectionPool(properties.getMaxIdleConnections(), properties.getKeepAliveDurationMill(), TimeUnit.MILLISECONDS))
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            //跳过验证
                            return true;
                        }
                    })
                    .build();
        }

    }

    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public SSLSocketFactory sslSocketFactory(X509TrustManager trustManager) {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            log.error(TAG, e);
        } catch (KeyManagementException e) {
            log.error(TAG, e);
        }
        return null;
    }

    public Map<String, Object> getForMap(String url, Map<String, Object> parms) throws Exception {
        return getForMap(url, parms, null);
    }

    public Map<String, Object> getForMap(String url, Map<String, Object> parms, Map<String, Object> headers) throws Exception {
        Map<String, Object> respMap = JSON.parseObject(getForString(url, parms, headers), Map.class);
        return respMap;
    }

    public String getForString(String url, Map<String, Object> parms, Map<String, Object> headers) throws Exception {
        return new String(get(url, parms, headers), UTF_8);
    }

    public byte[] get(String url, Map<String, Object> parms, Map<String, Object> headers) throws Exception {
        String realUrl = url;
        if (parms != null && parms.size() > 0) {
            String separator = "?";
            if (url.indexOf("?") >= 0) {
                separator = "&";
            }
            for (String key : parms.keySet()) {
                realUrl = realUrl + separator + key + "=" + parms.get(key).toString();
                separator = "&";
            }
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key).toString());
            }
        }
        String id = UUID.fastUUID().toString();
         log.info("Http Get {} : {}", id, realUrl);
        Request request = requestBuilder.url(realUrl).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() != HTTP_CODE_SUCCESS) {
                log.info("Http Get fail {} : {}", id, response.code());
                throw new HttpException(response.code());
            }
            byte[] resp = response.body().bytes();
            log.info("Http recv {} : {}", id, printByteArray(resp));
            return resp;
        }
    }

    public Map<String, Object> postJsonStringForMap(String url, String str, Map<String, Object> headers) throws Exception {
        String response = postJsonStringForString(url, str, headers);
        Map<String, Object> respMap = JSON.parseObject(response, Map.class);
        return respMap;
    }

    public Map<String, Object> postJsonForMap(String url, Object parms, Map<String, Object> headers) throws Exception {
        String response = postJsonForString(url, parms, headers);
        Map<String, Object> respMap = JSON.parseObject(response, Map.class);
        return respMap;
    }

    public String postJsonForString(String url, Object parms, Map<String, Object> headers) throws Exception {
        String json = null;
        if (parms != null) {
            json = JSON.toJSONString(parms);
        }
        return postJsonStringForString(url, json, headers);
    }

    public String postJsonStringForString(String url, String content, Map<String, Object> headers) throws Exception {
        return new String(post(url, content.getBytes(UTF_8), MEDIA_TYPE_JSON, headers), UTF_8);
    }

    public byte[] post(String url, byte[] content, String mediaType, Map<String, Object> headers) throws Exception {
        return post(url, content, MediaType.get(mediaType), headers);
    }


    public byte[] post(String url, byte[] content, MediaType mediaType, Map<String, Object> headers) throws Exception {
        Request.Builder requestBuilder = new Request.Builder();
        if (content != null) {
            RequestBody body = RequestBody.create(content, mediaType);
            requestBuilder.post(body);
        }
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key).toString());
            }
        }
        String id = UUID.fastUUID().toString();
        log.info("Http Post {} : {} | {}", id, url, JSONObject.toJSONString(printByteArray(content)));
        Request request = requestBuilder.url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() != HTTP_CODE_SUCCESS) {
                log.info("Http Post fail {}:{}:{}", id, response.code(),response.message());
                throw new HttpException(response.code());
            }
            byte[] resp = response.body().bytes();
            log.info("Http recv {} : {}", id, JSONObject.toJSONString(printByteArray(resp)));
            return resp;
        }
    }

    public String post(String url, byte[] content, Map<String, Object> headers) throws Exception {
        Request.Builder requestBuilder = new Request.Builder();
        if (content != null) {
            RequestBody body = RequestBody.create(content);
            requestBuilder.post(body);
        }
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key).toString());
            }
        }
        String id = UUID.fastUUID().toString();
        //log.info("Http Post {} : {} | {}", id, url, printByteArray(content));
        Request request = requestBuilder.url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() != HTTP_CODE_SUCCESS) {
                log.info("Http Post fail {} : {}", id, response.code());
                throw new HttpException(response.code());
            }
            byte[] resp = response.body().bytes();
            //log.info("Http recv {} : {}", id, printByteArray(resp));
            return new String(resp, UTF_8);
        }
    }

    public String postFormForString(String url, Map<String, Object> form, Map<String, Object> headers) throws Exception {
        //创建表单请求参数
        return new String(postForm(url, form, headers), UTF_8);
    }

    public byte[] postForm(String url, Map<String, Object> form, Map<String, Object> headers) throws Exception {
        //创建表单请求参数
        Request.Builder requestBuilder = new Request.Builder();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            if (entry.getValue() != null) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        FormBody formBody = builder.build();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key).toString());
            }
        }
        Request request = requestBuilder.url(url).post(formBody).build();
        String id = UUID.fastUUID().toString();
        log.info("Http Post {} : {} | {}", id, url, JSONObject.toJSONString(form));
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() != HTTP_CODE_SUCCESS) {
                log.info("Http Post fail {}:{}:{}", id, response.code(),response.message());
                throw new HttpException(response.code());
            }
            byte[] resp = response.body().bytes();
            log.info("Http recv {} : {}", id, JSONObject.toJSONString(printByteArray(resp)));
            return resp;
        }
    }

    private String printByteArray(byte[] b) {
        if (b == null) {
            return "null";
        }
        try {
            return new String(b, UTF_8);
        } catch (Exception e) {
            return JSONUtil.toJsonStr(b);
        }
    }
}
