package com.villa.cms.dto;

public class DBType2JavaTypeDTO {
    private String classname;//全限定名
    private String name;//简写类名
    private Class clz;//字节码

    public DBType2JavaTypeDTO(String classname, String name, Class clz) {
        this.classname = classname;
        this.name = name;
        this.clz = clz;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }
}
