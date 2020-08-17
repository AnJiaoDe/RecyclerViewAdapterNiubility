package com.cy.http.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class ParamsUtils {


    /**
     * 将传递进来的参数拼接成 url
     */
    public static String createUrlFromParams(String url, Map<String, Object> params) {
        if (url==null)return "";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, Object> urlParams : params.entrySet()) {
                //对参数进行 utf-8 编码,防止头信息传中文
                String urlValue = URLEncoder.encode(urlParams.getValue().toString(), "UTF-8");
                sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
        }
        return url;
    }


}
