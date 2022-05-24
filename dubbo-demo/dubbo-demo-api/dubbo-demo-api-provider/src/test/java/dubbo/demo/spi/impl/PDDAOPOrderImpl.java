package dubbo.demo.spi.impl;

import dubbo.demo.spi.IOrder;

/**
 * 包装类
 * @author william
 * @title
 * @desc
 * @date 2022/5/10
 **/
public class PDDAOPOrderImpl implements IOrder {

    private IOrder order;

    /**
     * 传入IOrder其他实现类对象
     * @param order
     */
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
        System.out.println("PDDAop Aop before");
        String stock = order.stock(signId);
        System.out.println(stock);
        System.out.println("PDDAopAop adter");
        return stock;
    }

}
