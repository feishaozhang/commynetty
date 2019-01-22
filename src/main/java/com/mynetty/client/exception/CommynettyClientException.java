package com.mynetty.client.exception;

/**
 * 客户端异常
 */
public class CommynettyClientException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommynettyClientException() {
    }

    public CommynettyClientException(String message) {
        super(message);
    }
}
