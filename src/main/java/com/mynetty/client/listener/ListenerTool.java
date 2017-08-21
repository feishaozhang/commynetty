package com.mynetty.client.listener;

import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ListenerTool {

    private static Logger logger = Logger.getLogger(ListenerTool.class);
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void callBack(final ClientCallback listener, final ClientCallback.OpType opType, final String message){
        if(listener == null){
            logger.error("ClientCallback listener is null");
            return ;
        }

        executor.execute(new Runnable() {
            public void run() {
                listener.onComplete(opType, message);
            }
        });
    }
}
