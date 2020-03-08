// Copyright (c) 2020 Cityline Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION 2.0.27
// ============================================================================
// CHANGE LOG
// 2.0.27 : 2020-03-03, derrick, creation
// ============================================================================
package com.demo.dl;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author derrick
 */
public class ConsumerTest {

    public static void main(final String[] args) {
        final ConsumerTest consumerTest = new ConsumerTest();
        consumerTest.addString(c -> {
            System.out.println(c.get("test"));
            System.out.println(c.get("value"));
        });
    }

    private void addString(final Consumer<Map<String, Object>> consumer) {
        final Map<String, Object> map = new HashMap<>();
        final Test test = new Test();
        test.setId("12331");
        map.put("test", test);
        map.put("value", 123);
        consumer.accept(map);
    }

    @Data
    static class Test {
        private String id;
        private String name;

        @Override
        public String toString() {
            return "Test: [ id = " + id + ", name = " + name + "]";
        }
    }

}
