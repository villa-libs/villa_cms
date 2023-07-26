package com.villa.cms.util;

import com.villa.cms.dto.DBType2JavaTypeDTO;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Locale;

public class ClassesUtil {
    /**
     * 数据库字段 -> java驼峰命名法
     * @param name
     * @return
     */
    public static String convertPropertyNameByDBName(String name){
        if(name.contains("_")){
            String[] names = name.split("_");
            String property = "";
            for (int i = 0; i < names.length; i++) {
                if(i==0){
                    property+=names[i];
                }else{
                    property+=StringUtil.toFirstUpperCase(names[i]);
                }
            }
            return property;
        }
        return name;
    }
    /**
     * 表明->类名
     *
     * @param tableName    表名
     * @param separator   分隔符 表的连接符,表名->类名时会将此连接符删除,并使用驼峰将前后进行拼接   如  dic_item --> DicItem
     * @return
     */
    public static String convertClassNameByTableName(String tableName, String separator) {
        //处理连接器
        String newSeparator = separator==null?" ":separator;
        String[] names = tableName.split(newSeparator);
        String classname = "";
        for (String str : names) {
            classname += StringUtil.toFirstUpperCase(str);
        }
        return classname;
    }

    public static DBType2JavaTypeDTO convertJAVATypeByDBType(String dbType) {
        switch(dbType.toUpperCase()){
            case "VARCHAR":
            case "TEXT":
            case "LONGTEXT":
            case "CHAR":
            case "JSON":
                return class2DTO(String.class);
            case "DECIMAL":
                return class2DTO(BigDecimal.class);
            case "TINYINT":
            case "SMALLINT":
            case "MEDIUMINT":
            case "INT":
                return class2DTO(Integer.class);
            case "BIGINT":
            case "INTEGER":
            case "TIMESTAMP":
                return class2DTO(Long.class);
            case "DATE":
            case "TIME":
            case "DATETIME":
                return class2DTO(Date.class);
            case "DOUBLE":
                return class2DTO(Double.class);
            case "FLOAT":
                return class2DTO(Float.class);
            case "BIT":
                return class2DTO(Boolean.class);
        }
        return null;
    }

    /**
     * class转模型
     * @param clz
     * @return
     */
    private static DBType2JavaTypeDTO class2DTO(Class<?> clz){
        return new DBType2JavaTypeDTO(clz.getName(),clz.getSimpleName(),clz);
    }

    /**
     * 是否是不用导包的类型
     * @return
     */
    public static boolean isJAVALangType(Class<?> clz){
        return  short.class.equals(clz)||
                byte.class.equals(clz)||
                int.class.equals(clz)||
                long.class.equals(clz)||
                float.class.equals(clz)||
                double.class.equals(clz)||
                boolean.class.equals(clz)||
                char.class.equals(clz)||
                Integer.class.equals(clz)||
                Long.class.equals(clz)||
                Float.class.equals(clz)||
                Double.class.equals(clz)||
                String.class.equals(clz)||
                Short.class.equals(clz)||
                Byte.class.equals(clz)||
                Character.class.equals(clz)||
                Boolean.class.equals(clz);
    }

    /**
     * 传入一个数据库类型字符串  返回一句导包代码
     * @param clz
     * @return
     */
    public static String handlerImport(String clz){
        DBType2JavaTypeDTO typeDTO = convertJAVATypeByDBType(clz);
        if(!isJAVALangType(typeDTO.getClz())){
            return "import\t"+typeDTO.getClassname()+";";
        }
        return "";
    }

    /**
     * 根据变量名 数据库类型 和注释 返回一句字段+注释的代码
     * @param type
     * @param comment
     * @return
     */
    public static String handlerField(String name,String type, String comment) {
        DBType2JavaTypeDTO typeDTO = convertJAVATypeByDBType(type);
        if(StringUtil.isNotEmptyOrNull(comment)){
            //添加注释符号  替换回车符为空格
            comment = "//"+comment.replace("\n"," ");
        }
        return "\tprivate\t"+typeDTO.getName()+"\t"+name+";\t"+comment;
    }

    /**
     * 生成getter/setter
     * @param name
     * @param type
     * @return
     */
    public static String handlerGetterAndSetter(String name, String type) {
        DBType2JavaTypeDTO typeDTO = convertJAVATypeByDBType(type);
        return "\tpublic "+typeDTO.getName()+" "+getter(name)+"(){\n"+
                "\t\treturn this."+name+";\n"+
                "\t}\n"+
                "\tpublic void "+setter(name)+"("+typeDTO.getName()+" "+name+"){\n"+
                "\t\tthis."+name+"="+name+";\n"+
                "\t}";
    }
    /**
     * 根据字段名生成set属性名
     * @param name
     * @return
     */
    public static String setter(String name) {
        //属性名只有一个字符
        if(name.length() == 1){
            return "set"+name.toUpperCase();
        }
        String one = name.substring(0, 1);
        String two = name.substring(1,2);
        //如果第一个字符和第二个字符都是小写  就将第一个转大写
        //如果第一个小写  第二个大写   不变
        //如果第一个大写  不变
        //所有只判断第一个和第二个是小写  其他都不变
        int i = one.charAt(0);
        int j = two.charAt(0);

        if (i >= 97 && i <=122&&j >= 97 && j <=122){
            one = one.toUpperCase();
            return "set"+one+name.substring(1);
        }else {
            return "set"+name;
        }
    }
    /**
     * 根据字段名生成get属性名
     * @param name
     * @return
     */
    public static String getter(String name) {
        //属性名只有一个字符
        if(name.length() == 1){
            return "get"+name.toUpperCase();
        }
        String one = name.substring(0, 1);
        String two = name.substring(1,2);
        //如果第一个字符和第二个字符都是小写  就将第一个转大写
        //如果第一个小写  第二个大写   不变
        //如果第一个大写  不变
        //所有只判断第一个和第二个是小写  其他都不变
        int i = one.charAt(0);
        int j = two.charAt(0);

        if (i >= 97 && i <=122&&j >= 97 && j <=122){
            one = one.toUpperCase();
            return "get"+one+name.substring(1);
        }else {
            return "get"+name;
        }
    }

    /**
     * 写文件
     * @param rules
     * @param packageStr
     */
    private static String content = "";
    public static void write(VelocityContext rules, String packageStr, String filename, String templatePath) {
        Template template = Velocity.getTemplate(templatePath, "UTF-8");
        StringWriter sw = new StringWriter();
        template.merge(rules,sw);

        System.out.println(sw);

        String path = packageStr.replace(".", "/");
        String url = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(".").getPath());
        path = new File(url).getParentFile().getParentFile().toPath()+"/src/main/java/"+path+"/"+filename;
        File file = new File(path);
        //不存在
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(sw.toString().getBytes());
            out.close();
            System.out.println("文件写成功:"+path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
