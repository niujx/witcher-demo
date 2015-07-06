package com.crock.config;

/**
 * Created by yanshi on 15-6-23.
 */
public class SeleniumConfig implements IConfig {

    @Override
    public Class getRealClass() {
        return getClass();
    }

    public void myOperation(){
        System.out.println("hello world");
    }
}
