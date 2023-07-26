import com.villa.cms.DBHandler;

import java.net.URLDecoder;

/**
 * @作者 微笑い一刀
 * @bbs_url https://blog.csdn.net/u012169821
 */
public class CmsHandler {
    public static void main(String[] args) {
            DBHandler handler = new DBHandler("jdbc:mysql://120.26.45.47:3306/tg?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8&useSSL=false",
                    "tg", "Dhnf7kfENdHSJwnr");
//            handler.setPackageName("com.villa.user");
//            /**
//             * 生成某一张表的全部
//             */
//            handler.autoOneByTableName("user");
            /**
             * 生成某一张表的基础文件 基础模型和基础dao
             */
//        handler.createBaseModelAndBaseMapper("user");
        handler.createVue("product");
    }
}
