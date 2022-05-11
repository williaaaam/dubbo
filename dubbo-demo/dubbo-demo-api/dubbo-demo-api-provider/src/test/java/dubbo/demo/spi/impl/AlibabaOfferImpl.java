package dubbo.demo.spi.impl;

import dubbo.demo.spi.IOffer;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/11
 **/
public class AlibabaOfferImpl implements IOffer {
    @Override
    public String company() {
        return "Alibaba";
    }

    @Override
    public String toString() {
        return "AlibabaImpl";
    }
}
