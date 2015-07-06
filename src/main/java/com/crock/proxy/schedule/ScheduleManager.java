package com.crock.proxy.schedule;

import javafx.util.Pair;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by yanshi on 15-7-2.
 */
public class ScheduleManager {

    private Scheduler scheduler;

    private static class ScheduleManagerHolder {
        static final ScheduleManager INSTANCE = new ScheduleManager();
    }

    public static ScheduleManager me() {
        return ScheduleManagerHolder.INSTANCE;
    }

    private ScheduleManager() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public ScheduleManager addJob(IjobCreator ijobCreator) {
        Pair<JobDetail, Trigger> jobDetailTriggerPair = ijobCreator.createJob();
        try {
            scheduler.scheduleJob
                    (jobDetailTriggerPair.getKey(), jobDetailTriggerPair.getValue());
           // scheduler.getListenerManager().addJobListener(new CheckProxyJobListener(), KeyMatcher.keyEquals(new JobKey("check_proxy_job", "check_group")));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ScheduleManager start() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return this;
    }


    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


}
