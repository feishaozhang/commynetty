package com.mynetty.engineerModule;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;

/**
 * Singleton patterm BaseComponentStarter
 * If you want to get the Instance of this Class
 * please use this method getBaseComponentStarter()
 * @version 1.0
 * @author  zsf
 */
public final class BaseComponentStarter {

    private static BaseComponentStarter starter;
    /**
     * is baseComponentstarter started
     */
    private volatile boolean isStarted;
    private Logger logger  = Logger.getLogger(this.getClass());

    /**
     * get the Instance of this class
     * @return BaseComponentStarter
     */
    public static BaseComponentStarter getBaseComponentStarter(){
        if(starter == null){
            synchronized (BaseComponentStarter.class){
                if(starter == null){
                    starter = new BaseComponentStarter();
                    return starter;
                }
            }
        }
        return starter;
    }

    private BaseComponentStarter() {
    }

    public void start(String log4jpath){
        if(!isStarted){
            synchronized (BaseComponentStarter.class){
                if(!isStarted){
                    initLog4j(log4jpath);
                    isStarted = true;
                    return;
                }
                logger.error("Component Module has get started");
            }
            return;
        }
        logger.error("Component Module has get started");
    }

    private void initLog4j(String log4jPath){
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(log4jPath);
        PropertyConfigurator.configure(url);
    }

}
