package nafos.core.entry;

import io.netty.handler.codec.http.HttpResponseStatus;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午5:49:12 
* 类说明 
*/
public class ResultStatus {

	@Override
	public String toString() {
		return "ResultStatus{" +
				"success=" + success +
				", ResponseStatus=" + ResponseStatus +
				'}';
	}

	private boolean success;
	
	private HttpResponseStatus ResponseStatus;

	public ResultStatus() {
	}

	public ResultStatus(boolean success, HttpResponseStatus responseStatus) {
		this.success = success;
		ResponseStatus = responseStatus;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public HttpResponseStatus getResponseStatus() {
		return ResponseStatus;
	}

	public void setResponseStatus(HttpResponseStatus responseStatus) {
		ResponseStatus = responseStatus;
	}
}
