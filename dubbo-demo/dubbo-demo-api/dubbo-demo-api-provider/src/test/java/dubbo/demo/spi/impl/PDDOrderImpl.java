package dubbo.demo.spi.impl;

import dubbo.demo.spi.IOrder;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/10
 **/
public class PDDOrderImpl implements IOrder {

    @Override
    public String provider() {
        return "PDD";
    }

    @Override
    public String stock(String signId) {
        return "PDD Stock";
    }

}
