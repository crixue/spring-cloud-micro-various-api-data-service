package com.xrj.springcloud.variousapidata.common;

/**
 * 响应码枚举类
 * @author crixus
 *
 */
public enum ResponseCodeEnum {
	
	SUCCESS(0, "SUCCESS"),
	ERROR(-1, "ERROR");
	
	private int status;
	private String desc;
	
	private ResponseCodeEnum(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
