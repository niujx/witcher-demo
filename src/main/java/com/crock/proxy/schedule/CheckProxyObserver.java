package com.crock.proxy.schedule;

import com.crock.proxy.Proxy;
import com.crock.proxy.ProxyResourceLoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yanshi on 15-6-29.
 */
public class CheckProxyObserver implements IjobCreator{

    private Logger logger = LogManager.getLogger(getClass());
    private ProxyResourceLoader proxyResourceLoader;
    private List<IQueueWatcher> queueWatchers = Lists.newArrayList();
    private List<Proxy> proxies = Lists.newCopyOnWriteArrayList();
    private Lock lock = new ReentrantLock();
    private JobDataMap jobDataMap = new JobDataMap();
    private CronScheduleBuilder cronScheduleBuilder;

    private CheckProxyObserver() {
        jobDataMap.put("queueWatchers", queueWatchers);
        jobDataMap.put("lock", lock);
        jobDataMap.put("proxies",proxies);
    }

    private static class CheckProxyObserverHolder {
        private static CheckProxyObserver INSTANCE = new CheckProxyObserver();
    }

    public static CheckProxyObserver me() {
        return CheckProxyObserverHolder.INSTANCE;
    }

    public void setCronExpression(String cronExpression) {
        cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
    }

    public void setProxyResourceLoader(ProxyResourceLoader proxyResourceLoader) {
        jobDataMap.put("proxyResourceLoader", this.proxyResourceLoader = proxyResourceLoader);
    }

    public void attach(IQueueWatcher queueWatcher) {
        Preconditions.checkNotNull(queueWatcher, "obs not null");
        Preconditions.checkNotNull(proxyResourceLoader, "proxyResourceLoader is null");

        queueWatchers.add(queueWatcher);
        logger.info("add queueWatcher proxies size : "+proxies.isEmpty());
        if (proxies.isEmpty()) {
            if (lock.tryLock()) {
                try {
                    proxies.addAll(proxyResourceLoader.loadProxies());
                } finally {
                    lock.unlock();
                }
            } else {
                logger.info("No lock！！！");
                queueWatcher.update(Collections.unmodifiableList(proxyResourceLoader.loadProxies()));
                return;
            }
        }

        logger.info("update queue");
        queueWatcher.update(Collections.unmodifiableList(proxies));

    }

    public Pair<JobDetail, Trigger> createJob() {
        Preconditions.checkNotNull(cronScheduleBuilder,"cronExpression is null");

        JobDetail jobDetail = JobBuilder.newJob(CheckProxyJob.class).withIdentity("check_proxy_job", "check_group")
                .setJobData(jobDataMap).build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("check_proxy_trigger", "check_group").startNow()
                .withSchedule(cronScheduleBuilder).build();

        return new Pair<>(jobDetail, trigger);
    }


}
