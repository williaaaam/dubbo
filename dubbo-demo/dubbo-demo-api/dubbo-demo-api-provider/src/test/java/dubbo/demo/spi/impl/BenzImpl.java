package dubbo.demo.spi.impl;

import dubbo.demo.spi.IName;
import org.apache.dubbo.common.URL;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
public class BenzImpl implements IName {


    public BenzImpl() {
        System.out.println("Benz Constructor");
    }

    static {
        //System.out.println("Benz static...");
    }

    @Override
    public String get(Integer userId) {
        return "benzImpl Integer get";
    }

    @Override
    public String toString() {
        return "Benz";
    }

    @Override
    public String get(URL url) {
        return "benzImpl url get";
    }
}
