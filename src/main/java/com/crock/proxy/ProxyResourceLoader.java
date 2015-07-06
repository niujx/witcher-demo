package com.crock.proxy;

import java.util.List;

/**
 * Created by yanshi on 15-6-24.
 */
public interface ProxyResourceLoader {

    List<Proxy> loadProxies();

    public void updateRepTime(Proxy proxy);


}
