package dubbo.demo.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/8
 **/
@SPI
public interface IName {

    /**
     * @param userId
     * @return
     */
    String get(Integer userId);


    /*@Adaptive("nameType")
    default String get(URL url) {
        return "路灯下的小女孩";
    }*/

    /**
     * 扩展点自适应
     * @param url
     * @return
     */
    @Adaptive("nameType")
    String get(URL url);
}
