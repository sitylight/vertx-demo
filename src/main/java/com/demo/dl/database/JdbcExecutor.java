// Copyright (c) 2020 Cityline Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION 2.0.27
// ============================================================================
// CHANGE LOG
// 2.0.27 : 2020-03-04, derrick, creation
// ============================================================================
package com.demo.dl.database;

import com.demo.dl.constant.AddressConstant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

/**
 * @author derrick
 */
public class JdbcExecutor extends AbstractVerticle {
    private JDBCClient jdbcClient;
    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
        jdbcClient = JDBCClient.create(vertx, DataBaseFactory.getInstance().getDataSource());
        vertx.eventBus().consumer(AddressConstant.DEMO_JDBC_EXECUTOR, message -> {
            final JsonObject jsonObject = (JsonObject) message.body();
            process(jsonObject, handler -> {
                if (handler.succeeded()) {
                    message.reply(handler.result());
                } else {
                    message.fail(500, "fail to get data");
                }
            });
        });
    }

    private void process(final JsonObject jsonObject, final Handler<AsyncResult<JsonArray>> handler) {
        jdbcClient.getConnection(sqlConnection -> {
            if (sqlConnection.succeeded()) {
                final SQLConnection connection = sqlConnection.result();
                connection.queryWithParams(jsonObject.getString("sql"), jsonObject.getJsonArray("tuple"), query -> {
                    if (query.succeeded()) {
                        handler.handle(Future.succeededFuture(new JsonArray(query.result().getResults())));
                    }
                    connection.close();
                });
            }
        });
    }
}
