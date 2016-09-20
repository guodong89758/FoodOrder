package com.foodorder.server.api;

/**
 * Created by guodong on 16/9/20.
 */
public class API_Food {

    private static volatile API_Food instance = null;

    private API_Food(){

    }

    public static API_Food ins(){
        if(instance == null){
            synchronized (API_Food.class){
                if(instance == null){
                    instance = new API_Food();
                }
            }
        }
        return instance;
    }

}
