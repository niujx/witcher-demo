package com.crock.proxy.schedule;

import javafx.util.Pair;
import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * Created by yanshi on 15-7-2.
 */
public interface IjobCreator {

    Pair<JobDetail, Trigger> createJob();
}
