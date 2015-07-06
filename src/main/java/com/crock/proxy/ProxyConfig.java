package com.crock.proxy;

/**
 * Created by yanshi on 15-6-25.
 */
public class ProxyConfig {

    private ProxyResourceLoader proxyResourceLoader;
    private ProxyRunStrategy proxyRunStrategy;

    public ProxyResourceLoader getProxyResourceLoader() {
        return proxyResourceLoader;
    }

    public void setProxyResourceLoader(ProxyResourceLoader proxyResourceLoader) {
        this.proxyResourceLoader = proxyResourceLoader;
    }

    public ProxyRunStrategy getProxyRunStrategy() {
        return proxyRunStrategy;
    }

    public void setProxyRunStrategy(ProxyRunStrategy proxyRunStrategy) {
        this.proxyRunStrategy = proxyRunStrategy;
    }

}
