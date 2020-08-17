package com.cy.http;//package com.cy.sdkstrategy_master.http;


import com.cy.http.utils.ParamsUtils;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class DownMultiRequestGenerator extends BaseRequestGenerator<DownMultiRequestGenerator> {
    private String pathFileToSave;
    private int count_thread;

    public DownMultiRequestGenerator file(String pathFileToSave) {
        this.pathFileToSave = pathFileToSave;
        return this;
    }

//    public DownMultiRequestGenerator thread(int count_thread) {
//        this.count_thread = count_thread;
//        return this;
//
//    }
    /**
     * 异步请求
     *
     * @param callback
     */
    public void enqueue(Callback callback) {
        if (callback == null) return;
        Call call = new CallDownMultiImpl(generateRequest(tag));
        callback.setTag(tag);
        call.enqueue(callback);
    }
    @Override
    public Request generateRequest(Object tag) {
        url = ParamsUtils.createUrlFromParams(baseUrl, params);
        return new DownMultiRequest.DownMultiBuilder()
                .setPathToSave(pathFileToSave)
//                .setCount_thread(count_thread)
                .setTag(tag)
                .setUrl(url)
                .setHeader(header)
                .setMethod(method)
                .build();
    }
}
