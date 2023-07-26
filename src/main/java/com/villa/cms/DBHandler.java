package com.villa.cms;

import com.villa.cms.util.ClassesUtil;
import com.villa.cms.util.DBUtil;
import com.villa.cms.util.TemplateUtil;
import com.villa.cms.util.VelocityInitializer;
import org.apache.velocity.VelocityContext;

import java.sql.Connection;
import java.util.List;

public class DBHandler {
    private Connection connection;

    /**
     * 一键生成 父子模型、父子DAO、父子XML、service、后台action、web api action
     */
    public void auto() {
        //获取所有表明
        List<String> tables = DBUtil.getTables(connection);
        tables.forEach(tableNam -> {
            autoOneByTableName(tableNam);
        });
    }

    public void autoOneByTableName(String tableName) {
        createBaseModelAndBaseMapper(tableName);
        createDAO(tableName);
        createModel(tableName);
        createDTO(tableName);
        createRepository(tableName);
        createService(tableName);
        createFacade(tableName);
        createAction(tableName);
    }

    public void createBaseModelAndBaseMapper(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules,rules.get("packageName")+".entity.base", rules.get("ClassName") + "Base.java", "entity/domainBase.java.vm");
        ClassesUtil.write(rules, rules.get("packageName")+".dao.base", rules.get("ClassName") + "BaseDAO.java", "dao/daoBase.java.vm");
        ClassesUtil.write(rules, rules.get("packageName")+".dao.base", rules.get("ClassName") + "BaseDAO.xml", "dao/daoBase.xml.vm");
    }
    public void createDAO(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".dao", rules.get("ClassName") + "DAO.java", "dao/dao.java.vm");
        ClassesUtil.write(rules, rules.get("packageName")+".dao", rules.get("ClassName") + "DAO.xml", "dao/dao.xml.vm");
    }
    /**
     * 创建子级模型
     */
    public void createModel(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".entity", rules.get("ClassName") + ".java", "entity/domain.java.vm");
    }
    public void createDTO(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".dto", rules.get("ClassName") + "PageDTO.java", "dto/pageDTO.java.vm");
    }
    public void createRepository(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".repository", rules.get("ClassName") + "Repository.java", "repository/repository.java.vm");
    }
    public void createService(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".service", rules.get("ClassName") + "Service.java", "service/service.java.vm");
    }
    public void createFacade(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".facade", rules.get("ClassName") + "Facade.java", "facade/facade.java.vm");
    }
    public void createAction(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".controller.api", "API"+rules.get("ClassName") + "Action.java", "controller/apiController.java.vm");
        ClassesUtil.write(rules, rules.get("packageName")+".controller", rules.get("ClassName") + "Action.java", "controller/controller.java.vm");
    }
    public void createVue(String tableName) {
        VelocityContext rules = handlerRules(tableName);
        ClassesUtil.write(rules, rules.get("packageName")+".vue.js", rules.get("classMapping") + ".js", "vue/api.js.vm");
        ClassesUtil.write(rules, rules.get("packageName")+".vue."+rules.get("classMapping"), "list.vue", "vue/list.vue.vm");
    }
    private VelocityContext handlerRules(String tableName){
        if(tableName.indexOf("-")!=-1){
            throw new RuntimeException("表面不能用横线连接，可以使用下划线_连接");
        }
        return TemplateUtil.handlerRules(connection,tableName,
                packageName,resultPackage, separator,resultDTO);
    }
    public DBHandler(String url, String username, String password) {
        this.connection = DBUtil.getConn(url, username, password);
        //初始化 velocity
        VelocityInitializer.initVelocity();
    }
    private String separator = "_";
    private String packageName = "com.villa";
    private String pageDTO = "PageDTO";
    private String resultDTO = "ResultDTO";
    private String resultPackage = "com.villa";

    public String getResultPackage() {
        return resultPackage;
    }

    public void setResultPackage(String resultPackage) {
        this.resultPackage = resultPackage;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPageDTO() {
        return pageDTO;
    }

    public void setPageDTO(String pageDTO) {
        this.pageDTO = pageDTO;
    }

    public String getResultDTO() {
        return resultDTO;
    }

    public void setResultDTO(String resultDTO) {
        this.resultDTO = resultDTO;
    }
}
