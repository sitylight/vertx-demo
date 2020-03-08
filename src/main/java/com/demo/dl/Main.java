package com.demo.dl;

import com.demo.dl.database.DataBaseFactory;
import com.demo.dl.database.JdbcExecutor;
import com.demo.dl.database.PgExecutor;
import com.demo.dl.engine.DataSourceVerticle;
import com.demo.dl.engine.DemoEngine;
import com.demo.dl.engine.Server;
import com.demo.dl.repository.DemoRepository;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        final VertxOptions vertxOptions = new VertxOptions();
        final Vertx vertx = Vertx.vertx(vertxOptions);
        vertxOptions.setWorkerPoolSize(20);
        vertxOptions.setEventLoopPoolSize(10);


        final ConfigStoreOptions storeOptions = new ConfigStoreOptions();

        storeOptions.setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", "config.yml"));
        final ConfigRetriever configRetriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(storeOptions));

        configRetriever.getConfig(cr -> {
            if (cr.failed()) {
                logger.debug("yaml file convert failed");
            } else {
                final JsonObject config = cr.result();
                vertx.deployVerticle(DataSourceVerticle.class.getName(), handler -> {
                    if (handler.succeeded()) {
                        DataBaseFactory.getInstance().init(config.getJsonObject("database")).flywayMigration(config.getJsonObject("hikari"));
                        vertx.deployVerticle(Server.class.getName(), new DeploymentOptions().setConfig(config));
                        vertx.deployVerticle(DemoEngine.class.getName());
                        vertx.deployVerticle(DemoRepository.class.getName());
                        vertx.deployVerticle(PgExecutor.class.getName());
                        vertx.deployVerticle(JdbcExecutor.class.getName());
                    } else {
                        logger.error("datasource not init");
                    }
                });
            }
        });
    }
}
