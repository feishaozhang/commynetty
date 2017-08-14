package com.mynetty;

import com.mynetty.server.Configuration;
import com.mynetty.engineerModule.BaseComponentStarter;
import com.mynetty.server.NettyStarter;

public class Engineer {
    public static void main(String[] args){
        NettyStarter nStarter = new NettyStarter();
        BaseComponentStarter bStarter = BaseComponentStarter.getBaseComponentStarter();
        //begin start the Netty Server
        try {
            //start base engineer
            bStarter.start(Configuration.LOG4J_PATH);

            //start netty engineer
            nStarter.bind(Configuration.SERVER_BIND_PORT);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
