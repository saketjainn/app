package com.crud.practice;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;

public class ReadUserApi{

     private static Pool client;
     static DbConnections db = new DbConnections();


    public static void attach(Router router){
        client = db.getClient();
        // Router router = Router.router(vertx);
        // router.route().handler(BodyHandler.create());

          router.get("/api/users").handler(ctx -> {
            client.getConnection(ar -> {
                if (ar.succeeded()) {
                    SqlConnection connection = ar.result();
                    connection.query("SELECT * FROM users").execute(ar2 -> {
                        if (ar2.succeeded()) {
                            RowSet<Row> result = ar2.result();
                            ctx.response().putHeader("content-type", "application/json")
                                .end(resultToJson(result).encodePrettily());
                        } else {
                            ctx.response().setStatusCode(500).end(ar2.cause().getMessage());
                        }
                        connection.close();
                    });
                } else {
                    ctx.response().setStatusCode(500).end(ar.cause().getMessage());
                }
            });
        });
        

     
    }
    private static JsonArray resultToJson(RowSet<Row> resultSet) {
        JsonArray jsonArray = new JsonArray();
        for (Row row : resultSet) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id", row.getInteger("id"));
            jsonObject.put("name", row.getString("name"));
            jsonObject.put("email", row.getString("email"));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    
}
