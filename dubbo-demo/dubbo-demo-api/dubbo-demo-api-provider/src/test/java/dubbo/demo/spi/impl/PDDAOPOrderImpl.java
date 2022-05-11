package dubbo.demo.spi.impl;

import dubbo.demo.spi.IOrder;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/10
 **/
public class PDDAOPOrderImpl implements IOrder {

    private IOrder order;

    public PDDAOPOrderImpl(IOrder order) {
        this.order = order;
    }

    @Override
    public String provider() {
        String s = order.provider();
        return s;
    }

    @Override
    public String stock(String signId) {
        System.out.println("Aop before");
        String stock = order.stock(signId);
        System.out.println(stock);
        System.out.println("Aop adter");
        return stock;
    }

}
