package com.crock.proxy.schedule;

import com.crock.proxy.Proxy;

import java.util.List;

/**
 * Created by yanshi on 15-6-30.
 */
public interface IQueueWatcher {

    void update(List<Proxy> proxyList);

}
