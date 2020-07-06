package com.demo.dl.engine;

import com.demo.dl.constant.AddressConstant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author derrick
 */
public class DemoEngine extends AbstractVerticle {
    private final static Logger logger = LoggerFactory.getLogger(DemoEngine.class);

    @Override
    public void start(final Promise<Void> promise) throws Exception {
        vertx.eventBus().consumer(AddressConstant.DEMO_ENGINE_SELECT, message -> {
            vertx.eventBus().request(AddressConstant.DEMO_REPOSITORY_SELECT, message.body(),  handler -> {
                if (handler.succeeded()) {
                    message.reply(handler.result().body());
                } else {
                    logger.error("fail reason: {}", ExceptionUtils.getStackTrace(handler.cause()));
                    message.fail(201, "fail to get demo");
                }
            });
        });
        vertx.eventBus().consumer(AddressConstant.DEMO_ENGINE_JDBC_SELECT, message -> {
            vertx.eventBus().request(AddressConstant.DEMO_REPOSITORY_JDBC_SELECT, message.body(),  handler -> {
                if (handler.succeeded()) {
                    message.reply(handler.result().body());
                } else {
                    logger.error("fail reason: {}", ExceptionUtils.getStackTrace(handler.cause()));
                    message.fail(201, "fail to get demo");
                }
            });
        });

        vertx.eventBus().consumer(AddressConstant.DEMO_ENGINE_INSERT, message -> {
            vertx.eventBus().request(AddressConstant.DEMO_REPOSITORY_INSERT, message.body(),  handler -> {
                if (handler.succeeded()) {
                    message.reply(handler.result().body());
                } else {
                    logger.error("fail reason: {}", ExceptionUtils.getStackTrace(handler.cause()));
                    message.fail(201, "fail to get demo");
                }
            });
        });
        promise.complete();
    }
}
