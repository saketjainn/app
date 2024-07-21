package com.crud.practice;

import io.vertx.core.Vertx;

import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;


public class DbConnections {
    
    Vertx vertx=Vertx.vertx();
    private Pool client;

    public DbConnections() {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
            .setPort(3306)
            .setHost("127.0.0.1")
            .setDatabase("vertx_crud")
            .setUser("root")
            .setPassword("root");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        client = Pool.pool(vertx, connectOptions, poolOptions);
    }

    public Pool getClient() {
        return client;
    }
}
