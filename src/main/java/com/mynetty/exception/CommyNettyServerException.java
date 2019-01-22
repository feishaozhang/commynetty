package com.mynetty.exception;

/**
 * 异常
 */
public class CommyNettyServerException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommyNettyServerException(String message) {
        super(message);
    }
}
