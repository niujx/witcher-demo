package com.crock.proxy.remote;

import com.crock.proxy.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by yanshi on 15-6-29.
 */
public class ProxyFactoryFacade implements IProxyFactoryFacade {

    private ProxyQueueFactory proxyQueueFactory;

    @PostConstruct
    public void init() {
        proxyQueueFactory = ProxyQueueFactory.create();
    }

    @Override
    public Proxy take(HostToken hostToken) {
        return proxyQueueFactory.take(hostToken);
    }

    @Override
    public boolean release(HostToken hostToken, Proxy proxy) {
        return proxyQueueFactory.release(hostToken, proxy);
    }

    @Override
    public List<Proxy> getProxies(HostToken hostToken) {
        return proxyQueueFactory.getProxyFactory(hostToken).getProxies();
    }
}
