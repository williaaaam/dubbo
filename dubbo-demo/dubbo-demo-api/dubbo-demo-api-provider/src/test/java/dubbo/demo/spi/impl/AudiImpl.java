package dubbo.demo.spi.impl;

import dubbo.demo.spi.IName;
import org.apache.dubbo.common.URL;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
public class AudiImpl implements IName {


    public AudiImpl() {
        System.out.println("audi constructor");
    }

    static {
        //System.out.println("Audi static");
    }

    @Override
    public String get(Integer userId) {
        return "Audi";
    }

    @Override
    public String get(URL url) {
        return "Audii";
    }


    @Override
    public String toString() {
        return "Audi";
    }
}
