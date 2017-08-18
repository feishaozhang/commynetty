package com.mynetty.server;

/**
 * 服务端配置文件
 */
public class Configuration {

    /**
     * 后台日志大小
     */
    public static int  SO_BACKLOG_SIZE = 1024;
    /**
     * 编码类型
     */
    public static String CHARSET_NAME = "UTF-8";
    /**
     * 服务器绑定端口
     */
    public static int SERVER_BIND_PORT = 8989;
    /**
     * log4j端口
     */
    public static String LOG4J_PATH = "log4j.properties";
    /**
     * 字符串解码缓冲区大小
     */
    public static int LINE_BASE_FRAME_DECODER_SIZE = 1024;
    /**
     * 固定字符解码器标示
     */
    public static String DELIMITER_DECODER_TAG = "$_";
    /**
     * 固定字符解码器包大小
     */
    public static int FIXED_LENGTH_SIZE = 20;

}
