package com.demo.dl.resource;

import io.vertx.ext.web.Router;

/**
 * @author derrick
 */
public interface Resource {
    Resource init(Router router);
}
