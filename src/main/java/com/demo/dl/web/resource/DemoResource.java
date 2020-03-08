// Copyright (c) 2020 Cityline Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION 2.0.27
// ============================================================================
// CHANGE LOG
// 2.0.27 : 2020-02-15, derrick, creation
// ============================================================================
package com.demo.dl.web.resource;

import com.demo.dl.constant.AddressConstant;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * @author derrick
 */
public class DemoResource implements Resource {
    private final static Logger logger = LoggerFactory.getLogger(DemoResource.class);
    private final static String ID_PARAMETER = "id";
    private final static String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
    private Vertx vertx;

    public DemoResource(final Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public DemoResource init(final Router router) {
        router.get("/demo/:" + ID_PARAMETER).produces("application/json").handler(this::getDemoById);
        router.get("/demo/jdbc/:" + ID_PARAMETER).produces("application/json").handler(this::getDemoByIdWithJdbc);
        router.post("/demo").produces(APPLICATION_JSON_CHARSET_UTF_8).handler(this::insertDemo);
        return this;
    }

    public static DemoResource create(final Vertx vertx) {
        return new DemoResource(vertx);
    }

    private void getDemoById(final RoutingContext routingContext) {
        final String id = routingContext.request().getParam(ID_PARAMETER);
        vertx.eventBus().request(AddressConstant.DEMO_ENGINE_SELECT, new JsonObject().put(ID_PARAMETER, id),
                this.<JsonArray>resultHandler(routingContext, resultBody ->
                        routingContext.response()
                                .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
                                .setStatusCode(200)
                                .end(resultBody.encode())
                )
        );
    }

    private void getDemoByIdWithJdbc(final RoutingContext routingContext) {
        final String id = routingContext.request().getParam(ID_PARAMETER);
        vertx.eventBus().request(AddressConstant.DEMO_ENGINE_JDBC_SELECT, new JsonObject().put(ID_PARAMETER, id),
                this.<JsonArray>resultHandler(routingContext, resultBody ->
                        routingContext.response()
                                .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
                                .setStatusCode(200)
                                .end(resultBody.encode())
                )
        );
    }

    private void insertDemo(final RoutingContext routingContext) {
        final JsonObject requestBody = routingContext.getBodyAsJson();
        vertx.eventBus().request(AddressConstant.DEMO_ENGINE_INSERT, requestBody, handler -> {
            if (handler.succeeded()) {
                logger.debug("result: {}", handler.result().body());
                routingContext.response().setStatusCode(200).end();
            } else {
                logger.error("error: {}", ExceptionUtils.getStackTrace(handler.cause()));
                routingContext.fail(500, handler.cause());
            }
        });
    }

    private <T> Handler<AsyncResult<Message<T>>> resultHandler(final RoutingContext routingContext, final Consumer<T> consumer) {
        return ar -> {
            if (ar.succeeded()) {
                consumer.accept(ar.result().body());
            } else {
                routingContext.fail(500, ar.cause());
            }
        };
    }
}
