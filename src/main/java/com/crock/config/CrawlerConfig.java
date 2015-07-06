package com.crock.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * Created by yanshi on 15-6-23.
 * HttpClient配置
 */
public class CrawlerConfig implements IConfig {

    private HashMap<String, Object> header = Maps.newHashMap();
    private HashMap<String, Object> cookies = Maps.newHashMap();
    private String url;
    private IConfig next;
    private Boolean isUseRedirect = true;

    public static CrawlerConfig me(String url) {
        return new CrawlerConfig(url);
    }

    public CrawlerConfig addHeader(String name, Object val) {
        header.put(name, val);
        return this;
    }

    public CrawlerConfig addCookie(String name, Object val) {
        cookies.put(name, val);
        return this;
    }

    public CrawlerConfig addNextStep(IConfig next) {
        this.next = next;
        return this;
    }

    public CrawlerConfig useRedirect(boolean isUse) {
        isUseRedirect = isUse;
        return this;
    }

    public IConfig next(){
        Preconditions.checkNotNull(next,"crawler next step is null");
        return next;
    }

    public boolean isNextStep() {
        return next != null;
    }



    public Boolean isUseRedirect() {
        return isUseRedirect;
    }

    public CrawlerConfig(String url) {
        this.url = url;
    }


    @Override
    public Class getRealClass() {
        return getClass();
    }

    public static void main(String[] args) {
        CrawlerConfig config = CrawlerConfig.me("");
        config.addNextStep(new SeleniumConfig());
        IConfig config1 = config.next();
        if(config1.getRealClass() == SeleniumConfig.class){
            SeleniumConfig.class.cast(config1).myOperation();
        }

        System.out.println(config.next().getRealClass().cast(config.next));
    }
}

