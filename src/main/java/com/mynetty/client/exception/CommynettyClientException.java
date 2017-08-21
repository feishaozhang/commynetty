package com.mynetty.client.exception;

/**
 * 客户端异常
 */
public class CommynettyClientException extends RuntimeException{

    public CommynettyClientException() {
    }

    public CommynettyClientException(String message) {
        super(message);
    }
}
