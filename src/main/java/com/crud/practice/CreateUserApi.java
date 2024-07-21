package com.crud.practice;


import io.vertx.ext.web.Router;

import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

public class CreateUserApi {

    private static Pool client;
    static DbConnections db = new DbConnections();

    
    public static void attach(Router router){
        client = db.getClient();

        //Router router = Router.router(vertx);
       // router.route().handler(BodyHandler.create());

        router.post("/api/users").handler(ctx -> {
            String name = ctx.getBodyAsJson().getString("name");
            String email = ctx.getBodyAsJson().getString("email");
            client.getConnection(ar -> {
                if (ar.succeeded()) {
                    SqlConnection connection = ar.result();
                    connection.preparedQuery("INSERT INTO users (name, email) VALUES (?, ?)")
                        .execute(Tuple.of(name, email), ar2 -> {
                            if (ar2.succeeded()) {
                                ctx.response().setStatusCode(201).end("data inserted");
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
}

