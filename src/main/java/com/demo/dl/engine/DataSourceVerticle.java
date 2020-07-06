package com.demo.dl.engine;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author derrick
 */
public class DataSourceVerticle extends AbstractVerticle {
    private final static Logger logger = LoggerFactory.getLogger(DataSourceVerticle.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer("getData", message -> {
            message.reply("event bus works");
            logger.info(message.body().toString());
        });
        startPromise.complete();
    }

}
