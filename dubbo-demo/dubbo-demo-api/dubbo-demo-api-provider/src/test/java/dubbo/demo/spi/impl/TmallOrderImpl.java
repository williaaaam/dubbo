package dubbo.demo.spi.impl;

import dubbo.demo.spi.IOrder;
import org.apache.dubbo.common.extension.Adaptive;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/10
 **/
@Adaptive
public class TmallOrderImpl implements IOrder {

    @Override
    public String provider() {
        return "TMALL";
    }

    @Override
    public String stock(String signId) {
        return "TMALL Stock";
    }

}
