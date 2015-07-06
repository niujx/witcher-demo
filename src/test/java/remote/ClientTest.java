package remote;

import com.crock.proxy.HostToken;
import com.crock.proxy.Proxy;
import com.crock.proxy.remote.IProxyFactoryFacade;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yanshi on 15-6-29.
 */
public class ClientTest {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-client-test.xml"});
        final IProxyFactoryFacade factoryFacade = context.getBean("proxyFactory", IProxyFactoryFacade.class);
        final HostToken hostToken = new HostToken("www.baidu.com");
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        final Random random = new Random(74);

        long count = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Proxy proxy = factoryFacade.take(hostToken);
                    try {
                        System.out.println(proxy);
                        TimeUnit.MILLISECONDS.sleep(random.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        factoryFacade.release(hostToken, proxy);
                    }

                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        System.out.println((System.currentTimeMillis()-count)/1000);

      /*  List<Proxy> proxyList = factoryFacade.getProxies(hostToken);
        for(Proxy proxy:proxyList){
            System.out.println(proxy);
        }*/
    }
}
