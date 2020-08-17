/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cy.http.utils;

import android.os.Build;

import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 */
public class SSLUtils {
    private static SSLSocketFactory trustAllSSlSocketFactory;

    public static SSLSocketFactory getTrustAllSSLSocketFactory() {
        if (trustAllSSlSocketFactory == null) {
            synchronized (SSLUtils.class) {
                if (trustAllSSlSocketFactory == null) {

                    // 信任所有证书
                    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }};
                    try {
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustAllCerts, null);
                        trustAllSSlSocketFactory = sslContext.getSocketFactory();
                    } catch (Throwable ex) {
                    }
                }
            }
        }

        return trustAllSSlSocketFactory;
    }
    public static void trustAllSSL(HttpURLConnection httpURLConnection){
        // try to fix bug: accidental EOFException before API 19
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            httpURLConnection.setRequestProperty("Connection", "close");
        }
        httpURLConnection.setInstanceFollowRedirects(false);
        if (httpURLConnection instanceof HttpsURLConnection) {
            SSLSocketFactory sslSocketFactory = SSLUtils.getTrustAllSSLSocketFactory();
            if (sslSocketFactory != null) {

                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslSocketFactory);
            }
        }
    }
}
