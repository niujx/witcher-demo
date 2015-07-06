package com.crock.proxy;

import java.util.Comparator;
import java.util.List;

/**
 * Created by yanshi on 15-6-24.
 */
public interface IProxyQueue extends Comparator<Proxy> {

    Proxy take();

    boolean release(Proxy proxy);

    List<Proxy> getProxies();

    boolean hasProxies();

}
