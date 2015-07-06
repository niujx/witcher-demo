package com.crock.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yanshi on 15-6-25.
 */
public class RunMain {

    public static void main(String[] args) throws InterruptedException {
        final HostToken hostToken = new HostToken("www.baidu.com");
        ProxyConfig proxyConfig = new ProxyConfig();
        proxyConfig.setProxyResourceLoader(new MySqlDBResourceLoader());
        proxyConfig.setProxyRunStrategy(new SimpleProxyRunStrategy());
        hostToken.setProxyConfig(proxyConfig);
        final ProxyQueueFactory proxyQueueFactory = ProxyQueueFactory.create();


        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        final  Random random = new Random(74);

        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Proxy proxy = proxyQueueFactory.take(hostToken);
                    try {
                        TimeUnit.SECONDS.sleep(random.nextInt(60));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        proxyQueueFactory.release(hostToken, proxy);
                    }

                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        List<Proxy> proxyList = proxyQueueFactory.getProxyFactory(hostToken).getProxies();
        for(Proxy proxy:proxyList){
            System.out.println(proxy);
        }
    }
}
