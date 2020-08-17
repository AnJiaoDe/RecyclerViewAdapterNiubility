package com.cy.http;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class Request {

    private Object tag;
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, Object> params;
    private Map<String, List<File>> paramsFile;
    private byte[] byteProto;
    private String bodyJson;

    Request(Builder builder) {
        this.tag = builder.getTag();
        this.url = builder.getUrl();
        this.method = builder.getMethod();
        this.headers=builder.getHeader();
        this.params=builder.getParams();
        this.byteProto=builder.getByteProto();
        this.paramsFile=builder.getParamsFile();
        this.bodyJson=builder.getBodyJson();
    }

    public static class Builder {
        private Object tag;
        private String url;
        private String method;
        private Map<String, String> header;
        private Map<String, Object> params;
        private byte[] byteProto;
        private Map<String, List<File>> paramsFile;
        private String bodyJson;

        public Builder() {
            this.method = "GET";
            this.tag = "tag";

        }

        public String getBodyJson() {
            return bodyJson;
        }

        public Builder setBodyJson(String bodyJson) {
            this.bodyJson = bodyJson;
            return this;
        }

        public Map<String, List<File>> getParamsFile() {
            return paramsFile;
        }

        public Builder setParamsFile(Map<String, List<File>> paramsFile) {
            this.paramsFile = paramsFile;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setHeader(Map<String, String> header) {
            this.header = header;
            return this;
        }
        public Builder setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder setTag(Object tag) {
            if (tag != null) this.tag = tag;
            return this;
        }

        public Builder setByteProto(byte[] byteProto) {
            this.byteProto = byteProto;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

        public Object getTag() {
            return tag;
        }

        public String getUrl() {
            return url;
        }

        public String getMethod() {
            return method;
        }

        public Map<String, String> getHeader() {
            return header;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public byte[] getByteProto() {
            return byteProto;
        }
    }

    public Map<String, List<File>> getParamsFile() {
        return paramsFile;
    }

    public Object getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public byte[] getByteProto() {
        return byteProto;
    }

    public Map<String, String> getHeader() {
        return headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getBodyJson() {
        return bodyJson;
    }
}
