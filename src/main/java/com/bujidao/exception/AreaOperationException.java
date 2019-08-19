package com.bujidao.exception;

public class AreaOperationException extends RuntimeException{

	private static final long serialVersionUID = 6533137181231743997L;

	public AreaOperationException(String message) {
		super(message);
	}

	public AreaOperationException() {
		super();
	}
	
}
