package com.mynetty.client;

public class ClientConfiguration {
    /**
     * 服务器IP地址
     */
    public static final String SERVER_HOSET = "127.0.0.1";
    /**
     * 服务器端口
     */
    public static final int SERVER_PORT = 8989;
    /**
     * log4j配置文件路径
     */
    public static final String log4jPath =  "log4jclient.properties";
    /**
     * 字符串解码大小
     */
    public static int LINE_BASE_FRAME_DECODER_SIZE = 1024;
    /**
     * 固定消息尾缀
     */
    public static String DELIMITER_DECODER_TAG = "$_";
    /**
     * 固定包长大小
     */
    public static int FIXED_LENGTH_SIZE = 20;
    /**
     * 客户端连接超时时间
     */
    public static int TIME_OUT_MILLIS = 3000;
}
