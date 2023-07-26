package com.villa.cms.dto;

/**
 * @作者 微笑い一刀
 * @bbs_url https://blog.csdn.net/u012169821
 */
public class ColumnDTO {
    /** 是否是主键 */
    private boolean pk;
    /** 是否是外键 */
    private boolean fk;
    /** 是否必填 */
    private boolean required;
    /** 是否自增 */
    private boolean autoIncrement;
    /** 字段名 数据库名称 */
    private String name;
    /** 模型中的属性名 如果带有下划线 _ 会生成驼峰  */
    private String propertyName;
    /** 外键类名 */
    private String fkClassName;
    /** 外键类名的属性名 */
    private String fkPropertyName;
    /** 映射的java类型 */
    private DBType2JavaTypeDTO javaType;
    /** 数据库类型 */
    private String dbType;
    /** 字段注释 */
    private String comment;
    private String getter;
    private String setter;
    private String fkSetter;
    private String fkGetter;
    private int sort;

    public String getFkSetter() {
        return fkSetter;
    }

    public void setFkSetter(String fkSetter) {
        this.fkSetter = fkSetter;
    }

    public String getFkGetter() {
        return fkGetter;
    }

    public void setFkGetter(String fkGetter) {
        this.fkGetter = fkGetter;
    }

    public boolean isFk() {
        return fk;
    }

    public void setFk(boolean fk) {
        this.fk = fk;
    }

    public String getFkClassName() {
        return fkClassName;
    }

    public void setFkClassName(String fkClassName) {
        this.fkClassName = fkClassName;
    }

    public String getFkPropertyName() {
        return fkPropertyName;
    }

    public void setFkPropertyName(String fkPropertyName) {
        this.fkPropertyName = fkPropertyName;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public DBType2JavaTypeDTO getJavaType() {
        return javaType;
    }

    public void setJavaType(DBType2JavaTypeDTO javaType) {
        this.javaType = javaType;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
