package com.villa.cms.util;

import com.villa.cms.dto.ColumnDTO;
import org.apache.velocity.VelocityContext;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateUtil {
    /**
     *
     * @param connection            链接对象
     * @param tableName             表名
     * @param packageName           基础包名
     * @param separator             表名分隔符
     * @param resultDTO             结果集对象类名
     * @return
     */
    public static VelocityContext handlerRules(Connection connection, String tableName,String packageName,String resultPackage,String separator, String resultDTO) {
        //类名处理
        String ClassName = ClassesUtil.convertClassNameByTableName(tableName, separator);
        //字段处理
        List<ColumnDTO> columns = DBUtil.getTableInfo(connection, tableName);
        String tableComment = DBUtil.getTableComment(connection, tableName);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("classMapping" , tableName.replace(separator, "-"));
        //是否需要上传组件
        List<ColumnDTO> uploaderes = columns.stream().filter(columnDTO ->
            columnDTO.getPropertyName().indexOf("Url")!=-1 ||
            columnDTO.getPropertyName().indexOf("url")!=-1 ||
            columnDTO.getPropertyName().indexOf("logo")!=-1 ||
            columnDTO.getPropertyName().indexOf("Logo")!=-1
        ).collect(Collectors.toList());
        if(uploaderes.size()>0){
            velocityContext.put("hasUploader",uploaderes.size()>0);
            velocityContext.put("uploadPropertyName",uploaderes.get(0).getPropertyName());
        }
        //是否存在外键
        long count = columns.stream().filter(columnDTO -> columnDTO.isFk()).count();
        velocityContext.put("hasFK" , count>0);

        velocityContext.put("tableComment" , tableComment);
        velocityContext.put("tableName" , tableName);
        velocityContext.put("ClassName" , ClassName);
        velocityContext.put("classname" , StringUtil.toFirstLowerCase(ClassName));
        velocityContext.put("packageName" , packageName);
        velocityContext.put("resultDTO" , resultDTO);
        velocityContext.put("resultPackage" , resultPackage);
        //----------------------------------VUE原带$的变量-------------------------------------------
        velocityContext.put("message" , "$message");
        velocityContext.put("forceUpdate" , "$forceUpdate");
        //-----------------------------------------------------------------------------
        Set<String> imports = new HashSet<>();
        columns.stream().forEach(column -> {
            //主键处理
            if(column.isPk()){
                //是否自增
                if(column.isAutoIncrement()){
                    velocityContext.put("autoIncrement",column.isAutoIncrement());
                }
                velocityContext.put("idType" , column.getJavaType().getName());
                velocityContext.put("idName" , column.getName());
                velocityContext.put("idGetter" , column.getGetter());
                return;
            }
            if(column.getName().equals("version")){
                //判断是否存在乐观锁
                velocityContext.put("hasLock" , true);
            }
            //处理模型类中的导入数据
            String importStr = ClassesUtil.handlerImport(column.getDbType());
            if(StringUtil.isEmptyOrNull(importStr)){
                return;
            }
            imports.add(importStr);
        });
        //字段排序
        columns.sort(Comparator.comparing(ColumnDTO::getSort));
        velocityContext.put("imports" , imports);
        velocityContext.put("columns" , columns);
        return velocityContext;
    }
}
