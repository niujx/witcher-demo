package com.crock.proxy;

import com.crock.proxy.schedule.CheckProxyObserver;
import com.crock.proxy.schedule.IQueueWatcher;
import com.crock.proxy.schedule.ScheduleManager;
import com.google.common.base.Preconditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yanshi on 15-6-25.
 */
public class ProxyQueueFactory {

    private ConcurrentHashMap<String, IProxyQueue> proxyFactoryCache = new ConcurrentHashMap();
    private CheckProxyObserver checkProxyObserver = CheckProxyObserver.me();
    private Lock lock = new ReentrantLock();
    private Logger logger = LogManager.getLogger(getClass());

    private ProxyQueueFactory() {
        checkProxyObserver.setProxyResourceLoader(new MySqlDBResourceLoader());
        checkProxyObserver.setCronExpression("0 */5 * * * ?");
        ScheduleManager.me().addJob(checkProxyObserver).start();
    }

    public static ProxyQueueFactory create() {
        return ProxyQueueFactoryHolder.INSTANCE;
    }

    private static class ProxyQueueFactoryHolder {
        static ProxyQueueFactory INSTANCE = new ProxyQueueFactory();
    }

    public IProxyQueue getProxyFactory(HostToken hostToken) {
        Preconditions.checkNotNull(hostToken, "hostToken is not null");
        IProxyQueue proxyQueue = proxyFactoryCache.get(hostToken.getToken());
        if (proxyQueue == null) {
            logger.info("create new queue");
            proxyQueue = new BasicProxyQueue(hostToken.getProxyConfig());
            IProxyQueue tmp = proxyFactoryCache.putIfAbsent(hostToken.getToken(), proxyQueue);
            if (tmp != null) {
                proxyQueue =tmp;
            }

        }

        lock.lock();

        try {
            if (!proxyQueue.hasProxies())
                checkProxyObserver.attach(IQueueWatcher.class.cast(proxyQueue));
        } finally {
            lock.unlock();
        }

        return proxyQueue;
    }

    public Proxy take(HostToken hostToken) {
        return getProxyFactory(hostToken).take();
    }

    public boolean release(HostToken hostToken, Proxy proxy) {
        return getProxyFactory(hostToken).release(proxy);
    }

}
