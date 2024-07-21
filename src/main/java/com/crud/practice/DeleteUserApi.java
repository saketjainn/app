package com.crud.practice;
import io.vertx.ext.web.Router;

import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

public class DeleteUserApi{

    private static Pool client;
    static DbConnections db = new DbConnections();

    
    public static void attach(Router router) {
        client = db.getClient();

        // Router router = Router.router(vertx);
        // router.route().handler(BodyHandler.create());

        
        
        router.delete("/api/users/:id").handler(ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            client.getConnection(ar -> {
                if (ar.succeeded()) {
                    SqlConnection connection = ar.result();
                    connection.preparedQuery("DELETE FROM users WHERE id = ?")
                        .execute(Tuple.of(id), ar2 -> {
                            if (ar2.succeeded()) {
                                ctx.response().setStatusCode(204).end();
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
