package com.xy.logload.domain;

import java.io.Serializable;

public class BaseResult <T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 474698411474237016L;
	
	private boolean success;
	private String code;
	private T data;
	private String message;
	
	public BaseResult(boolean success, String code, T data, String message){
		this.success=success;
		this.code=code;
		this.data=data;
		this.message=message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
