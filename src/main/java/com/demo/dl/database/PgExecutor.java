// Copyright (c) 2020 Cityline Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION 2.0.27
// ============================================================================
// CHANGE LOG
// 2.0.27 : 2020-02-29, derrick, creation
// ============================================================================
package com.demo.dl.database;

import com.demo.dl.constant.AddressConstant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author derrick
 */
public class PgExecutor extends AbstractVerticle {
    private final static Logger logger = LoggerFactory.getLogger(PgExecutor.class);

    private PgPool client;

    @Override
    public void start() throws Exception {
        client = PgPool.pool(vertx, DataBaseFactory.getInstance().getPgConnectOptions(), DataBaseFactory.getInstance().getPoolOptions());

        vertx.eventBus().consumer(AddressConstant.DEMO_PG_EXECUTOR, message -> {
            final JsonObject messageObject = (JsonObject) message.body();
            process(messageObject, handler -> {
                message.reply(handler.result());
            });
        });

    }

    private void process(final JsonObject messageObject, final Handler<AsyncResult<JsonArray>> handler) {
        client.getConnection(connect -> {
            if (connect.succeeded()) {
                final SqlConnection connection = connect.result();
//                connection.query(messageObject.getString("sql"), queryResult -> {
//                    if (queryResult.succeeded()) {
//                        handler.handle(Future.succeededFuture(queryResult.result()));
//                    }
//                    connection.close();
//                });
                final Tuple tuple = Tuple.tuple(messageObject.getJsonArray("tuple").getList());
                final String sql = messageObject.getString("sql");
                final String type = messageObject.getString("type");
                    connection.preparedQuery(sql, tuple , queryResult -> {
                        if (queryResult.succeeded()) {
                            final RowSet<Row> rows = queryResult.result();
                            final JsonArray result = new JsonArray();
                            rows.forEach(row -> {
                                final JsonObject jsonObject = new JsonObject();
                                final int size = row.size();
                                for (int i = 0; i < size; i++) {
                                    final String columnName = row.getColumnName(i);
                                    final Object value = row.getValue(i);
                                    if (value instanceof LocalDateTime) {
                                        jsonObject.put(columnName, ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
                                    } else {
                                        jsonObject.put(columnName, value);
                                    }
                                }
                                result.add(jsonObject);
                            });
                            handler.handle(Future.succeededFuture(result));
                        } else {
                            logger.error("pg execute error:{}", ExceptionUtils.getStackTrace(queryResult.cause()));
                        }
                        connection.close();
                    });
                }

        });
    }

}
