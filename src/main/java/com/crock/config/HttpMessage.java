package com.crock.config;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * Created by yanshi on 15-6-23.
 */
public class HttpMessage implements IMessage {

    HttpContext context;
    Header httpHeader;


    @Override
    public void send(Object o) {
    }
}
