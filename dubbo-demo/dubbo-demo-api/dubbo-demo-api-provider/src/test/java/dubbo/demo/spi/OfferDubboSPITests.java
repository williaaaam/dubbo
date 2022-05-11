package dubbo.demo.spi;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
public class OfferDubboSPITests {

    /**
     * Dubbo SPI兼容
     */
    @Test
    public void testDubboSPI() {

        ExtensionLoader<IOffer> loader = ExtensionLoader.getExtensionLoader(IOffer.class);

        //System.out.println(loader.getDefaultExtension().company());

        //loader.getSupportedExtensions().forEach(System.out::println);

        //loader.getLoadedExtensionInstances().forEach(System.out::println);
        // 回触发所有实现类的初始化，并执行初始化方法
        // jdk spi 不会执行类的init方法
        //Class.forName(line, true, classLoader),
        // 如果没有配置key,默认实现类名全部小写作为key
        System.out.println(loader.getExtension("alibabaofferimpl").company());
    }

    @Test
    public void testJDkSPI() {

        ServiceLoader<IOffer> load = ServiceLoader.load(IOffer.class);

        Iterator<IOffer> iterator = load.iterator();

        while (iterator.hasNext()) {
            // Class.forName(cn, false, loader);
            IOffer next = iterator.next();
            System.out.println(next);
        }
    }

}
