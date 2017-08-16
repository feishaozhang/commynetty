package com.mynetty.commom.msgpack.model;

public class ProtocolConfig {
    /**
     * netty消息校验码三部分
     * 1、0xccdc 固定值，表明消息是系统的协议消息
     * 2、主版本号：1~255 1byte
     * 3、次版本号:1~255 1byte
     * crcCode = oxccdc+主版本号+次版本号
     */
    public static int crcCode = 0xccdc0101;
}
