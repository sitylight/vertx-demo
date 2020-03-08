package com.demo.dl.constant;

/**
 * @author derrick
 * @date 2020/3/3
 */
public final class AddressConstant {
    private final static String PACKAGE = "com.demo.dl";

    // demo engine
    public final static String DEMO_ENGINE_SELECT = PACKAGE + "#engine#select";
    public final static String DEMO_ENGINE_JDBC_SELECT = PACKAGE + "#engine#jdbc#select";
    public final static String DEMO_ENGINE_INSERT = PACKAGE + "#engine#insert";


    //repository
    public final static String DEMO_REPOSITORY_SELECT = PACKAGE + "#demorepository#select";
    public final static String DEMO_REPOSITORY_JDBC_SELECT = PACKAGE + "#demorepository#jdbc#select";
    public final static String DEMO_REPOSITORY_INSERT = PACKAGE + "#demorepository#insert";


    //pg executor
    public final static String DEMO_PG_EXECUTOR = "pg-executor";
    public final static String DEMO_JDBC_EXECUTOR = "jdbc-executor";
}
