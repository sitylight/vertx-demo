package com.demo.dl.repository;

import com.demo.dl.constant.AddressConstant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.LinkedList;

/**
 * @author derrick
 */
public class DemoRepository extends AbstractVerticle{
    @Override
    public void start() throws Exception {

        vertx.eventBus().consumer(AddressConstant.DEMO_REPOSITORY_SELECT, message -> vertx.eventBus()
                .request(AddressConstant.DEMO_PG_EXECUTOR, getDemoMsgMap((JsonObject) message.body()), handlerRequest(message)));

        vertx.eventBus().consumer(AddressConstant.DEMO_REPOSITORY_JDBC_SELECT, message -> vertx.eventBus()
                .request(AddressConstant.DEMO_JDBC_EXECUTOR, getDemoJdbcMap((JsonObject) message.body()), handlerRequest(message)));

        vertx.eventBus().consumer(AddressConstant.DEMO_REPOSITORY_INSERT, message -> vertx.eventBus()
                .request(AddressConstant.DEMO_PG_EXECUTOR, createDemo((JsonObject) message.body()), handlerRequest(message)));
    }


    private JsonObject getDemoJdbcMap(final JsonObject messageBody) {
        final JsonObject msgJsonObject = new JsonObject();
        final LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.add(messageBody.getString("id"));
        msgJsonObject.put("sql", "select * from tb_demo where id = ?");
        msgJsonObject.put("tuple", linkedList);
        msgJsonObject.put("type", "select");
        return msgJsonObject;
    }
    private JsonObject getDemoMsgMap(final JsonObject messageBody) {
        final JsonObject msgJsonObject = new JsonObject();
        final LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.add(messageBody.getString("id"));
        msgJsonObject.put("sql", "select * from tb_demo where id = $1");
        msgJsonObject.put("tuple", linkedList);
        msgJsonObject.put("type", "select");
        return msgJsonObject;
    }

    private JsonObject createDemo(final JsonObject messageBody) {
        final JsonObject jsonObject = new JsonObject();

        final LinkedList<Object> linkedList = new LinkedList<>();
        final String sql = "insert into tb_demo(id, content, updated_on) values($1, $2, now())";
        messageBody.stream().filter(value -> !value.getKey().equals("updated_on")).forEach(value -> linkedList.add(value.getValue()));
//        linkedList.add(LocalDateTime.now());
        jsonObject.put("type", "insert");
        jsonObject.put("sql", sql);
        jsonObject.put("tuple", linkedList);
        return jsonObject;
    }

    private Handler<AsyncResult<Message<Object>>> handlerRequest(final Message<Object> message) {
        return ar -> {
            if (ar.succeeded()) {
                message.reply(ar.result().body());
            } else {
                message.fail(500, ar.cause().getMessage());
            }
        };
    }
}
