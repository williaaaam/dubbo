package dubbo.demo.spi.impl;

import dubbo.demo.spi.IName;
import org.apache.dubbo.common.URL;

/**
 * SPI AOP
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
public class BenzWrapperImpl implements IName {

    private IName iName;

    public BenzWrapperImpl(IName name) {
        this.iName = name;
    }

    @Override
    public String get(Integer userId) {
        System.out.println("before");
        String s = iName.get(userId);
        System.out.println("after");
        return s;
    }

    @Override
    public String get(URL url) {
        return "benzWrapper";
    }

    @Override
    public String toString() {
        return "Benz aop";
    }
}
