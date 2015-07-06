package com.crock.proxy;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by yanshi on 15-6-25.
 */
public class HostToken implements Serializable {

    private String token;
    private ProxyConfig proxyConfig;

    public HostToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostToken hostToken = (HostToken) o;
        return Objects.equal(token, hostToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }
}
