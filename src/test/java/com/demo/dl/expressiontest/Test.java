// Copyright (c) 2020 Cityline Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION 2.0.27
// ============================================================================
// CHANGE LOG
// 2.0.27 : 2020-03-04, derrick, creation
// ============================================================================
package com.demo.dl.expressiontest;

/**
 * @author derrick
 */
@FunctionalInterface
public interface Test<T> {
    void handle(T event);
}
