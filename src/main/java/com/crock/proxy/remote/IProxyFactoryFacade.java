package com.crock.proxy.remote;

import com.crock.proxy.HostToken;
import com.crock.proxy.Proxy;

import java.util.List;

/**
 * Created by yanshi on 15-6-29.
 */
public interface IProxyFactoryFacade {

    Proxy take(HostToken hostToken);

    boolean release(HostToken hostToken, Proxy proxy);

    List<Proxy> getProxies(HostToken hostToken);

}
