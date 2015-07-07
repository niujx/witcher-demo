package com.crock.proxy;

import com.crock.proxy.schedule.IQueueWatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by yanshi on 15-6-24.
 */
public class BasicProxyQueue implements IProxyQueue, IQueueWatcher {

    private Logger logger = LogManager.getLogger(getClass());
    private PriorityQueue<Proxy> proxyQueue;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private ProxyRunStrategy proxyRunStrategy;
    private List<Proxy> usedCache = Lists.newCopyOnWriteArrayList();
    private Random random = new Random(74);
    private static final long EXPIRED_TIME = 5L * 60 * 1000;
    // private static final  long EXPIRED_TIME = 10;

    public BasicProxyQueue(ProxyConfig proxyConfig) {

        if (proxyConfig == null) {
            proxyConfig = new ProxyConfig();
        }

        if (proxyConfig.getProxyRunStrategy() == null) {
            this.proxyRunStrategy = new SimpleProxyRunStrategy();
        } else {
            this.proxyRunStrategy = proxyConfig.getProxyRunStrategy();
        }
    }

    @Override
    public Proxy take() {
        lock.lock();
        try {

            releaseExpiredProxy();

            while (proxyQueue.isEmpty()) {
                try {
                    if (usedCache.isEmpty()) {
                        condition.await();
                    }

                    if (proxyQueue.isEmpty()) {
                        int index = random.nextInt(usedCache.size() - 1);
                        Proxy prototype = usedCache.get(index);
                        prototype.addCount();
                        Proxy temp = prototype.clone();
                        if (logger.isDebugEnabled()) {
                            logger.debug("get used proxy [" + temp + "] proxyQueue size=" + proxyQueue.size());
                        }
                        return temp;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }


            Proxy proxy = proxyQueue.poll();
            proxy.addCount();
            if (logger.isDebugEnabled()) {
                logger.debug("get proxy :" + proxy);
            }

            if (proxyRunStrategy != null) {
                proxyRunStrategy.calculateNice(proxy);
            }

            if (!usedCache.contains(proxy)) {
                usedCache.add(proxy);
            }

            return proxy;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean release(Proxy proxy) {
        Preconditions.checkNotNull(proxy, "release proxy  not null");
        lock.lock();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("release proxy :" + proxy);
            }

            if (proxy.isTemp() || proxyQueue.contains(proxy)) return true;
            proxy.setLastUseTime(System.currentTimeMillis());
            usedCache.remove(proxy);
            return proxyQueue.add(proxy);
        } finally {
            condition.signalAll();
            lock.unlock();
        }
    }


    private void releaseExpiredProxy() {
        final long now = System.currentTimeMillis();
        Iterator<Proxy> proxyIterator = usedCache.iterator();
        while (proxyIterator.hasNext()) {
            Proxy entity = proxyIterator.next();
            if (now - entity.getLastUseTime() > EXPIRED_TIME) {
                logger.info("proxy [" + entity + "] expired ");
                release(entity);
            }
        }
    }

    @Override
    public List<Proxy> getProxies() {
        return Collections.unmodifiableList(new ArrayList(proxyQueue));
    }

    @Override
    public boolean hasProxies() {
        return proxyQueue != null;
    }

    @Override
    public int compare(Proxy o1, Proxy o2) {
        return ComparisonChain.start().compare(o1.getNice(), o2.getNice()).result();
    }

    @Override
    public void update(List<Proxy> proxyList) {
        lock.lock();
        try {
            proxyQueue = new PriorityQueue(proxyList.size(), this);
            proxyQueue.addAll(proxyList);
        } finally {
            lock.unlock();
        }
    }
}
