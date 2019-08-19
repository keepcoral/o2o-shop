package com.bujidao.exception;

public class ProductCategoryOperationException 
	extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2096826748534584091L;

	public ProductCategoryOperationException() {
		super();
	}

	public ProductCategoryOperationException(String message) {
		super(message);
	}
	
}
