package com.crock.proxy.schedule;

import com.crock.proxy.Proxy;
import com.crock.proxy.ProxyResourceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * Created by yanshi on 15-7-2.
 */
public class CheckProxyJob implements Job {

    private Logger logger = LogManager.getLogger(getClass());
    private ProxyResourceLoader proxyResourceLoader;
    private List<Proxy> proxies;
    private List<IQueueWatcher> queueWatchers;
    private Lock lock;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("check proxy start ======== proxy queue size :" + queueWatchers.size());
        executor();
        logger.info("check proxy end ======== proxy queue size :" + queueWatchers.size());
    }

    public void executor() {

        lock.lock();
        try {
            proxies.clear();
            proxies.addAll(proxyResourceLoader.loadProxies());

            for (Proxy proxy : proxies) {
                checkReq(proxy);
            }
        } finally {
            lock.unlock();
        }

        for (IQueueWatcher watcher : queueWatchers) {
            watcher.update(Collections.unmodifiableList(proxies));
        }

        logger.info("update proxy queue size :" + queueWatchers.size());
    }


    private void checkReq(Proxy proxy) {
        try {
            Proxy proxy_t = new Proxy("61.174.8.5", 8888);
            java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxy_t.getHost(), proxy_t.getPort()));
            URLConnection connection = new URL("http://www.baidu.com").openConnection(p);
            HttpURLConnection httpURLConnection = HttpURLConnection.class.cast(connection);
            httpURLConnection.setRequestMethod("GET");
            long ms = System.currentTimeMillis();
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
            /*    try (BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                    String textLine;
                    while ((textLine = bufferedInputStream.readLine()) != null) {
                        System.out.println(textLine);
                    }
                }*/
                proxy.setMs(System.currentTimeMillis() - ms);

            } else {
                proxies.remove(proxy);
            }

            proxyResourceLoader.updateRepTime(proxy);
            logger.info("check proxy:"+proxy);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setProxyResourceLoader(ProxyResourceLoader proxyResourceLoader) {
        this.proxyResourceLoader = proxyResourceLoader;
    }

    public void setQueueWatchers(List<IQueueWatcher> queueWatchers) {
        this.queueWatchers = queueWatchers;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public void setProxies(List<Proxy> proxies) {
        this.proxies = proxies;
    }
}
