package com.villa.cms.util;

public class StringUtil {
    public static boolean isEmptyOrNull(String str){
        return str==null||"".equals(str.trim());
    }
    public static boolean isNotEmptyOrNull(String str){
        return str!=null&&!"".equals(str.trim());
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String toFirstUpperCase(String str){
        if(isEmptyOrNull(str)){
            return "";
        }
        char[] cs=str.toCharArray();
        if(cs[0]>=95){
            cs[0]-=32;
        }
        return String.valueOf(cs);
    }
    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String toFirstLowerCase(String str){
        char[] cs=str.toCharArray();
        if(cs[0]<95){
            cs[0]+=32;
        }
        return String.valueOf(cs);
    }
}
