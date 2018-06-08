package com.result.base.entry;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = -32874872141403803L;

	private boolean success;
	private String error;
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Result() {
		super();
	}

	public Result(boolean success) {
		this.success = success;
	}

	public Result error(String error) {
		this.error = error;
		return this;
	}

	public Result(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

}
