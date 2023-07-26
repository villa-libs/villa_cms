package com.villa.cms.util;

import com.villa.cms.dto.ColumnDTO;
import org.apache.commons.lang3.ClassUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
    /**
     * 获取链接对象
     */
    public static Connection getConn(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("数据库驱动注册失败.");
        }
    }
    /**
     * 获取当前使用的数据库名
     */
    public static String getDatabaseName(Connection conn) {
        try {
            String sql = "select database();";
            PreparedStatement state = conn.prepareStatement(sql);
            ResultSet set = state.executeQuery();
            if(set.next()) {
                return set.getString("database()");
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库名称集获取失败.");
        }
        return null;
    }
    /**
     * 获取所有表名
     */
    public static List<String> getTables(Connection conn) {
        List<String> list = new ArrayList<>();
        try {
            String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=?";
            PreparedStatement state = conn.prepareStatement(sql);
            state.setObject(1,getDatabaseName(conn));
            ResultSet query = state.executeQuery();
            while(query.next()){
                list.add(query.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库表名获取失败.");
        }
        return list;
    }
    public static String getTableComment(Connection conn,String tableName) {
        try {
            String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=? and `table_name` = ?";
            PreparedStatement state = conn.prepareStatement(sql);
            state.setObject(1,getDatabaseName(conn));
            state.setObject(2,tableName);
            ResultSet query = state.executeQuery();
            while(query.next()){
                return query.getString("TABLE_COMMENT");
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库表名获取失败.");
        }
        return null;
    }
    /**
     * 获取某表的字段名
     * @return
     */
    public static List<ColumnDTO> getTableInfo(Connection conn,String tableName){
        List<ColumnDTO> list = new ArrayList<>();
        try {
            String sql = "SELECT COLUMN_NAME,COLUMN_KEY,DATA_TYPE,COLUMN_COMMENT,IS_NULLABLE,ORDINAL_POSITION,EXTRA FROM `information_schema`.`columns`" +
                    "WHERE `table_schema` = ?" + "AND `table_name` = ?;";
            PreparedStatement state = conn.prepareStatement(sql);
            state.setObject(1, getDatabaseName(conn));
            state.setObject(2, tableName);
            ResultSet query = state.executeQuery();
            while(query.next()){
                ColumnDTO column = new ColumnDTO();
                column.setName(query.getString("COLUMN_NAME"));
                column.setPropertyName(ClassesUtil.convertPropertyNameByDBName(column.getName()));
                column.setDbType(query.getString("DATA_TYPE"));
                column.setComment(query.getString("COLUMN_COMMENT"));
                column.setJavaType(ClassesUtil.convertJAVATypeByDBType(column.getDbType()));
                column.setPk("PRI".equalsIgnoreCase(query.getString("COLUMN_KEY")));
                column.setRequired("NO".equalsIgnoreCase(query.getString("IS_NULLABLE")));
                column.setSort(query.getInt("ORDINAL_POSITION"));
                column.setAutoIncrement("auto_increment".equalsIgnoreCase(query.getString("EXTRA")));
                //是否是外键
                if(column.getPropertyName().indexOf("Id")!=-1){
                    String propertyName = column.getPropertyName().split("Id")[0];
                    column.setFk(true);
                    column.setFkClassName(StringUtil.toFirstUpperCase(propertyName));
                    column.setFkPropertyName(propertyName);
                    column.setFkSetter(ClassesUtil.setter(propertyName));
                    column.setFkGetter(ClassesUtil.getter(propertyName));
                }
                //getter/setter方法名
                column.setSetter(ClassesUtil.setter(column.getPropertyName()));
                column.setGetter(ClassesUtil.getter(column.getPropertyName()));

                list.add(column);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库获取字段名称失败.");
        }
        return list;
    }
}
