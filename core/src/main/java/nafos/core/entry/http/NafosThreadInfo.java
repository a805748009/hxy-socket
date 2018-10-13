package nafos.core.entry.http;

/**
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月26日 下午3:49:01 
* 类说明 
*/
public class NafosThreadInfo {

	private NafosRequest nafosRequest;

	private NafosRespone nafosRespone;

	public NafosThreadInfo(NafosRequest nafosRequest) {
		this.nafosRequest = nafosRequest;
	}


	public NafosThreadInfo() {
	}

	public NafosRequest getNafosRequest() {
		return nafosRequest;
	}

	public void setNafosRequest(NafosRequest nafosRequest) {
		this.nafosRequest = nafosRequest;
	}

	public NafosRespone getNafosRespone() {
		return nafosRespone;
	}

	public void setNafosRespone(NafosRespone nafosRespone) {
		this.nafosRespone = nafosRespone;
	}
}
