package com.crock.config;

/**
 * Created by yanshi on 15-6-23.
 */
public interface IMessage<T> {

    void send(T t);

}
