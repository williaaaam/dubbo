package dubbo.demo.spi.impl;

import dubbo.demo.spi.IOffer;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/11
 **/
public class JDOfferImpl implements IOffer {

    static {
        System.out.println("Jd offer static");
    }

    {
        System.out.println("jd offer instance");
    }

    @Override
    public String company() {
        return "JD";
    }


    @Override
    public String toString() {
        return "JDOffer";
    }
}
