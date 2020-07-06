package com.demo.dl.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author derrick
 */
public class DataBaseFactory {
    private PoolOptions poolOptions;
    private PgConnectOptions pgConnectOptions;
    private DataSource dataSource;

    private static class Singleton {
        static DataBaseFactory INSTANCE = new DataBaseFactory();
    }

    public static DataBaseFactory getInstance() {
        return Singleton.INSTANCE;
    }

    public DataBaseFactory init(final JsonObject properties) {
//        final JsonObject properties = properties.getJsonObject("database");
        pgConnectOptions = new PgConnectOptions();
        pgConnectOptions
                .setHost(properties.getString("host"))
                .setDatabase(properties.getString("name"))
                .setPassword(properties.getString("password"))
                .setUser(properties.getString("user"))
                .setPort(properties.getInteger("port"));
        poolOptions = new PoolOptions().setMaxSize(10);
        return this;
    }

    public void flywayMigration(final JsonObject config) {
        final Properties properties = new Properties();
        properties.putAll(config.getMap());
        final HikariConfig hikariConfig = new HikariConfig(properties);
        dataSource = new HikariDataSource(hikariConfig);
        final Migration migration = new Migration();
        migration.setDataSource(dataSource).doMigration();
    }

    public PgConnectOptions getPgConnectOptions() {
        return pgConnectOptions;
    }

    public PoolOptions getPoolOptions() {
        return poolOptions;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
