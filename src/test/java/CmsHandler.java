import com.villa.cms.DBHandler;

public class CmsHandler {
    public static void main(String[] args) {
        DBHandler handler = new DBHandler("jdbc:mysql:///xx?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8&useSSL=false",
                "root", "123456");
        //设置基础包 默认com.villa
        handler.setPackageName("com.villa");
        //生成某一张表的全部
        handler.autoOneByTableName("user");
        //生成某一张表的父级模型 父级dao/xml 自定义内容写在子类或子xml中，不会被此方法覆盖
        handler.createBaseModelAndBaseMapper("user");
        //生成某个表的后台CRUD 需要集合模板项目使用
        handler.createVue("product");
        //生成所有表的所有后台资源 从模型到控制器 不包含vue/js文件
        handler.auto();
    }
}