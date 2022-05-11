package dubbo.demo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
public class SPITests {

    /**
     * 1. 不支持查找provider
     * 2. 不支持ioc和aop
     */
    @Test
    public void testJdkSPI() {
        ServiceLoader<IName> iNames = ServiceLoader.load(IName.class);
        Iterator<IName> iterator = iNames.iterator();
        while (iterator.hasNext()) {
            // Java SPI会加载并实例化 providers
            // 如果spi文件里面的providers不存在，则会报错
            // System.out.println(iterator.next());
        }
    }

    /**
     *
     */
    @Test
    public void testDubboSPIWithAOP() {
        ExtensionLoader<IName> extensionLoader = ExtensionLoader.getExtensionLoader(IName.class);
        //Set<IName> extensions = extensionLoader.getSupportedExtensionInstances();
//        Set<String> supportedExtensions = extensionLoader.getSupportedExtensions();
        // 打印的是 toString方法
//        supportedExtensions.forEach(System.out::println);

//        System.out.println("---------");
        // null
//        System.out.println(extensionLoader.getDefaultExtension());
        // exception
        //System.out.println(extensionLoader.getExtension("xxxxxx"));
        // No such extension dubbo.demo.spi.IName
//        System.out.println(extensionLoader.getExtension("dubbo.demo.spi.impl.AudiImpl"));

        // System.out.println("*****");
        // 实现AOP
        System.out.println(extensionLoader.getExtension("benz"));


    }

    /**
     * 依赖注入
     * 要想实现IName IOC, 配置文件中不能出现实现INamed AOP的类
     */
    @Test
    public void testDubboSPIWithIOC() {
        ExtensionLoader<IName> extensionLoader = ExtensionLoader.getExtensionLoader(IName.class);
        IName ioc = extensionLoader.getExtension("benzIoc");


        Map<String, String> hashMap = new HashMap();
        // hashMap.put("nameType", "bmw");
        // hashMap.put("nameType", "audi");
        hashMap.put("nameType", "benz");
        URL url = new URL("", "", 0, hashMap);
        // print
        // audi constructor
        // Audii
        System.out.println(ioc.get(url));
    }


}
