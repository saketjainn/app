package com.crud.practice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error ->
    System.out.println("Unhandled:"+ error)
    );
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> System.out.println("Failed to deploy:"+ err))
      .onSuccess(id ->
      System.out.println("Deployed :"+ MainVerticle.class.getSimpleName()+ "with id :"+id)
      );
  }


   @Override
  public void start(Promise<Void> startPromise) throws Exception {

     final Router restApi= Router.router(vertx);
    
    restApi.route()
    .failureHandler(handleFailure())
    .handler(BodyHandler.create());
    

    CreateUserApi.attach(restApi);
    ReadUserApi.attach(restApi);
    UpdateUserApi.attach(restApi);
    DeleteUserApi.attach(restApi);
    //SearchUserApi.attach(restApi);


    vertx.createHttpServer()
    .requestHandler(restApi)
    .exceptionHandler(error->{System.out.println("HTTP server erroe:"+error);})
    .listen(8081, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8081");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
    private Handler<RoutingContext> handleFailure() {
    return errorContext->{
      if(errorContext.response().ended()){
        //if resquest is cancled by client
        return;
      }
      System.out.println("Router error :"+ errorContext.failure());
      
      errorContext.response()
       .setStatusCode(500)
       .end(new JsonObject().put("message", "somethong went wrong").toBuffer());
    };
  }
}
