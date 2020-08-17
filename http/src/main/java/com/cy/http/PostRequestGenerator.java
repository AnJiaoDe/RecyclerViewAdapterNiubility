package com.cy.http;//package com.cy.sdkstrategy_master.http;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class PostRequestGenerator extends BaseRequestGenerator<PostRequestGenerator> {
    protected byte[] byteProto;
    protected Map<String, List<File>> paramsFile = new HashMap<>();
    protected String bodyJson;

    public PostRequestGenerator body(String bodyJson) {
        this.bodyJson=bodyJson;
        return this;
    }
    /**
     * protobuf请求时使用
     *
     * @param bytes
     * @return
     */
    public PostRequestGenerator byteProto(byte[] bytes) {
        this.byteProto = bytes;
        return this;
    }

    public PostRequestGenerator file(String key, File file) {
        List<File> list = new ArrayList<>();
        list.add(file);
        paramsFile.put(key, list);
        return this;
    }

    public PostRequestGenerator files(String key, List<File> list) {
        paramsFile.put(key, list);
        return this;
    }
    public PostRequestGenerator files(String key, File[] files) {
        List<File> list = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            list.add(files[i]);
        }
        paramsFile.put(key, list);
        return this;
    }

    @Override
    public Request generateRequest(Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.setTag(tag)
                .setUrl(url)
                .setHeader(header)
                .setMethod(method)
                .setParams(params)
                .setByteProto(byteProto)
                .setParamsFile(paramsFile)
                .setBodyJson(bodyJson)
                .build();

    }
}
