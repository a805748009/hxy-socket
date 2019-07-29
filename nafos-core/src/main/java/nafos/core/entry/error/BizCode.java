package nafos.core.entry.error;

import io.netty.handler.codec.http.HttpResponseStatus;

public class BizCode extends HttpResponseStatus {
    public BizCode(int code, String reasonPhrase) {
        super(code, reasonPhrase);
    }
}
