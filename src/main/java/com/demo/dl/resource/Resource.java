// Copyright (c) 2020 Cityline Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION 2.0.27
// ============================================================================
// CHANGE LOG
// 2.0.27 : 2020-02-15, derrick, creation
// ============================================================================
package com.demo.dl.resource;

import io.vertx.ext.web.Router;

/**
 * @author derrick
 */
public interface Resource {
    Resource init(Router router);
}
