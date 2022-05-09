package dubbo.demo.spi.impl;

import dubbo.demo.spi.IName;
import org.apache.dubbo.common.URL;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
public class BenzIOCImpl implements IName {

    /**
     * 实际上是Dubbo为我们生成的代理对象 xxx$Adaptive
     */
    private IName iName;

    @Override
    public String get(Integer userId) {
        System.out.println("before");
        String s = iName.get(userId); // 实际上执行的时候， ExtensionLoader.getExtensionLoader()
        System.out.println("after");
        return s;
    }

    @Override
    public String get(URL url) {
        return iName.get(url);
    }

    public void setiName(IName iName) {
        this.iName = iName;
    }

    /**
     * Spring容器中管理的对象
     *
     * @param iName
     */
    public void setSpringIName(IName iName) {
        this.iName = iName;
    }

}
