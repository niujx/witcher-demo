package com.crock.proxy.schedule;

import com.crock.proxy.Proxy;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import java.util.List;

/**
 * Created by yanshi on 15-7-2.
 */
public class CheckProxyJobListener implements JobListener {

    private static final String NAME = "check_proxy_listener";
    private Logger logger = LogManager.getLogger(getClass());
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        List<Proxy> storeProxies = List.class.cast( context.get("proxies"));
        for(Proxy proxy :storeProxies){
            logger.info("stored:"+proxy);
        }

    }
}
