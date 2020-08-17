package com.cy.http;//package com.cy.sdkstrategy_master.http;


import com.cy.http.utils.ParamsUtils;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class GetRequestGenerator extends BaseRequestGenerator<GetRequestGenerator> {


    @Override
    public Request generateRequest(Object tag) {
        Request.Builder builder = new Request.Builder();
        url = ParamsUtils.createUrlFromParams(baseUrl, params);
        return builder.setTag(tag)
                .setUrl(url)
                .setHeader(header)
                .setMethod(method)
                .build();
    }
}
