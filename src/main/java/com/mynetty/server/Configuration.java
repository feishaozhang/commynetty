package com.mynetty.server;

public class Configuration {

    /**
     * BACKLOG Size
     */
    public static int  SO_BACKLOG_SIZE = 1024;
    /**
     * encode type
     */
    public static String CHARSET_NAME = "UTF-8";
    /**
     * Server Bind port
     */
    public static int SERVER_BIND_PORT = 8989;
    /**
     * log4jPath
     */
    public static String LOG4J_PATH = "log4j.properties";
    /**
     * LineBaseFramedecodersize
     */
    public static int LINE_BASE_FRAME_DECODER_SIZE = 1024;
    /**
     * delimiter decoder tag
     */
    public static String DELIMITER_DECODER_TAG = "$_";
}
