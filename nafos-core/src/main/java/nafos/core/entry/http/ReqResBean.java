package nafos.core.entry.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.bootStrap.handle.http.NsRespone;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月26日 下午3:49:01
 * 类说明
 */
public class ReqResBean {

    private NsRequest nafosRequest;

    private NsRespone nafosRespone;

    public ReqResBean(NsRequest nafosRequest, NsRespone nafosRespone) {
        this.nafosRequest = nafosRequest;
        this.nafosRespone = nafosRespone;
    }

    public ReqResBean(NsRequest nafosRequest) {
        this.nafosRequest = nafosRequest;
        this.nafosRespone = new NsRespone(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }

    public NsRequest getNafosRequest() {
        return nafosRequest;
    }

    public void setNafosRequest(NsRequest nafosRequest) {
        this.nafosRequest = nafosRequest;
    }

    public NsRespone getNafosRespone() {
        return nafosRespone;
    }

    public void setNafosRespone(NsRespone nafosRespone) {
        this.nafosRespone = nafosRespone;
    }
}
