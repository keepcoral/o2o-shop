package com.bujidao.exception;

public class WechatAuthOperationException extends RuntimeException{

	private static final long serialVersionUID = 6533137181231743997L;

	public WechatAuthOperationException(String message) {
		super(message);
	}

	public WechatAuthOperationException() {
		super();
	}
	
}
