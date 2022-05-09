package dubbo.demo.spi.impl;

import dubbo.demo.spi.IName;
import org.apache.dubbo.common.URL;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
public class BMWImpl implements IName {


    public BMWImpl() {
        System.out.println("BMW constructor");
    }

    static {
        //System.out.println("Audi static");
    }

    @Override
    public String get(Integer userId) {
        return "BMW";
    }

    @Override
    public String get(URL url) {
        return "BMW";
    }


    @Override
    public String toString() {
        return "BMW";
    }
}
