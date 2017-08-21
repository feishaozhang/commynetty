package com.mynetty.client;

import com.mynetty.client.exception.CommynettyClientException;
import com.mynetty.client.model.ClientStartParams;

public class CommynettyApi {

    public static void start(ClientStartParams parms) throws CommynettyClientException{
       Client client =  Client.getInstance();
//       client.start(parms);
    }

    public static void sendMessage(){
        Client client =  Client.getInstance();
//        client.start(parms);
    }

    public static void stop(){

    }

}
