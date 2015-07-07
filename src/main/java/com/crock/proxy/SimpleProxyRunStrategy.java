package com.crock.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by yanshi on 15-6-25.
 */
public class SimpleProxyRunStrategy implements ProxyRunStrategy {

    private Logger logger = LogManager.getLogger(getClass());


    private static final double REQ_TIME_WEIGHT = 1;
    private static final double WORK_CONCURRENT_COUNT_WEIGHT = 1;
    //private static final double HEALTH_WEIGHT = -0.5;
    private static final double WORKING_INTERVAL = 0.05;
    private static final int FIVE_MINUTES = 5 * 60;

    @Override
    public void calculateNice(Proxy proxy) {

        long currentUseTime = 0L;
        if (proxy.getLastUseTime() > 0L) {
            long time = System.currentTimeMillis();
            currentUseTime = FIVE_MINUTES - ((time - proxy.getLastUseTime()) / 1000 );
            if(logger.isDebugEnabled())
                logger.debug(time+":"+proxy.getLastUseTime()+":"+((time - proxy.getLastUseTime()) / 1000 +":"+currentUseTime));
        }

        double workingInterval = currentUseTime * WORKING_INTERVAL;
        double workCount = proxy.getUseCount() * WORK_CONCURRENT_COUNT_WEIGHT;
        double reqTime = proxy.getMs()/(double)500 * REQ_TIME_WEIGHT;
        // double health = proxy.getHealth() * HEALTH_WEIGHT;

        double nice = workingInterval + workCount + reqTime;
        proxy.setNice((int) Math.round(nice));
    }
}
