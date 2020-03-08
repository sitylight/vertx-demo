// Copyright (c) 2020 Cityline Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION 2.0.27
// ============================================================================
// CHANGE LOG
// 2.0.27 : 2020-03-04, derrick, creation
// ============================================================================
package com.demo.dl.expressiontest;

import java.util.function.Consumer;

/**
 * @author derrick
 */
public class MainTest {
    public static void main(final String[] args) {
        final MainTest mainTest = new MainTest();
        mainTest.process(test -> {
            System.out.println("|||||||");
            System.out.println(test.result());
        });

        mainTest.processConsumer(c -> {
            System.out.println(c.result());
        });
    }

    private void process(final Test<TestResult<String>> test) {
        System.out.println("--------");
        final String value = "1233";
        test.handle(new TestResult<String>() {
            @Override
            public String result() {
                return value;
            }

            @Override
            public boolean succeed() {
                return true;
            }
        });
        test.handle(new TestResult<String>() {
            @Override
            public String result() {
                return "111111";
            }

            @Override
            public boolean succeed() {
                return true;
            }
        });
    }

    private void processConsumer(final Consumer<TestResult<String>> consumer) {
        final String value = "000000";
        consumer.accept(new TestResult<String>() {
            @Override
            public String result() {
                return value;
            }

            @Override
            public boolean succeed() {
                return true;
            }
        });
    }
}
