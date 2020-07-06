package com.demo.dl.engine;

import com.demo.dl.resource.DemoResource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author derrick
 */
public class Server extends AbstractVerticle {
    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    @Override
    public void start(final Promise<Void> promise) throws Exception {
        logger.debug("start server verticle ------------------");
        final Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        DemoResource.create(vertx).init(router);
        final HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router);
        httpServer.listen(this.config().getJsonObject("http").getInteger("port"), deploy -> {
            if (deploy.succeeded()) {
                logger.debug("server verticle is deployed");
                promise.complete();
            } else {
                logger.error("fail to deploy server");
                promise.fail("fail to deploy server");
            }
        });
    }
}
