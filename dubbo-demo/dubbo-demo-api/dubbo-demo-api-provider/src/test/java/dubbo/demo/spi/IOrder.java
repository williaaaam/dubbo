package dubbo.demo.spi;

import org.apache.dubbo.common.extension.SPI;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
@SPI("tmall")
public interface IOrder {

    String provider();


    String stock(String signId);

}
