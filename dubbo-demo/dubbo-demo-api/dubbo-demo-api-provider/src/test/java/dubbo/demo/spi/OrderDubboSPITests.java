package dubbo.demo.spi;

import com.google.common.base.Stopwatch;
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
public class OrderDubboSPITests {


    @Test
    public void testSPI() {

        ExtensionLoader<IOrder> loader = ExtensionLoader.getExtensionLoader(IOrder.class);

        IOrder order = loader.getDefaultExtension();

        System.out.println(order.getClass().getName());

        System.out.println(order.stock("2323jj23"));
    }

    /**
     * Print:
     * <p>
     * Aop before
     * Aop after
     * -------
     * Aop before
     * Aop after
     * TMALL Stock
     * ---------
     * Aop before
     * Aop after
     * TMALL Stock
     * ----------
     * Aop before
     * Aop after
     * JD Stock
     */
    @Test
    public void testAOP() {

        ExtensionLoader<IOrder> loader = ExtensionLoader.getExtensionLoader(IOrder.class);

//        IOrder order = loader.getExtension("pdd");
//
//        order.stock("23232323po201212");
//
//        System.out.println("-------");

//        IOrder extension = loader.getExtension("true");
//
//        extension.stock("233333");
//
//        System.out.println("---------");
//
//        loader.getDefaultExtension().stock("hahaha");
//
//        System.out.println("----------");
//
//        loader.getExtension("jd").stock("2022");

        //System.out.println("----");

        //loader.getExtension("tmall").stock("2222");

        IOrder extension = loader.getExtension("pdd");

        extension.stock("2323");

    }

}
