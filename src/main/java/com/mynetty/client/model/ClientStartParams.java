package com.mynetty.client.model;

/**
 * 客户端启动参数集
 */
public class ClientStartParams {
    private String host;
    private int port;
    private int crcCode;
    private String auth;

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ClientStartParams(String host, int port, int crcCode, String auth) {
        this.host = host;
        this.port = port;
        this.crcCode = crcCode;
        this.auth = auth;
    }

    public ClientStartParams() {
    }
}
