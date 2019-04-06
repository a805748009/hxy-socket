package nafos.bootStrap.handle.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;


public class BuildHttpObjectAggregator extends HttpObjectAggregator {
    public BuildHttpObjectAggregator(int maxContentLength) {
        super(maxContentLength);
    }

    public BuildHttpObjectAggregator(int maxContentLength, boolean closeOnExpectationFailed) {
        super(maxContentLength, closeOnExpectationFailed);
    }

    protected void aggregate(FullHttpMessage aggregated, HttpContent content) throws Exception {
        if (content instanceof LastHttpContent) {
            ((NsRequest) aggregated).setTrailingHeaders(((LastHttpContent) content).trailingHeaders());
        }

    }

    protected FullHttpMessage beginAggregation(HttpMessage start, ByteBuf content) throws Exception {
        assert !(start instanceof FullHttpMessage);

        HttpUtil.setTransferEncodingChunked(start, false);
        Object ret;
        if (start instanceof HttpRequest) {
            ret = new NsRequest((HttpRequest) start, content, null);
        } else {
            if (!(start instanceof HttpResponse)) {
                throw new Error();
            }

            ret = new BuildHttpObjectAggregator.AggregatedFullHttpResponse((HttpResponse) start, content, null);
        }

        return (FullHttpMessage) ret;
    }

    private static final class AggregatedFullHttpResponse extends AggregatedFullHttpMessage implements FullHttpResponse {
        AggregatedFullHttpResponse(HttpResponse message, ByteBuf content, HttpHeaders trailingHeaders) {
            super(message, content, trailingHeaders);
        }

        public FullHttpResponse copy() {
            return this.replace(this.content().copy());
        }

        public FullHttpResponse duplicate() {
            return this.replace(this.content().duplicate());
        }

        public FullHttpResponse retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }

        public FullHttpResponse replace(ByteBuf content) {
            DefaultFullHttpResponse dup = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), content, this.headers().copy(), this.trailingHeaders().copy());
            dup.setDecoderResult(this.decoderResult());
            return dup;
        }

        public FullHttpResponse setStatus(HttpResponseStatus status) {
            ((HttpResponse) this.message).setStatus(status);
            return this;
        }

        public HttpResponseStatus getStatus() {
            return ((HttpResponse) this.message).status();
        }

        public HttpResponseStatus status() {
            return this.getStatus();
        }

        public FullHttpResponse setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        public FullHttpResponse retain(int increment) {
            super.retain(increment);
            return this;
        }

        public FullHttpResponse retain() {
            super.retain();
            return this;
        }

        public FullHttpResponse touch(Object hint) {
            super.touch(hint);
            return this;
        }

        public FullHttpResponse touch() {
            super.touch();
            return this;
        }

        public String toString() {
            return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
        }


    }

    public static class AggregatedFullHttpRequest extends AggregatedFullHttpMessage implements FullHttpRequest {
        public AggregatedFullHttpRequest(HttpRequest request, ByteBuf content, HttpHeaders trailingHeaders) {
            super(request, content, trailingHeaders);
        }

        public FullHttpRequest copy() {
            return this.replace(this.content().copy());
        }

        public FullHttpRequest duplicate() {
            return this.replace(this.content().duplicate());
        }

        public FullHttpRequest retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }

        public FullHttpRequest replace(ByteBuf content) {
            DefaultFullHttpRequest dup = new DefaultFullHttpRequest(this.protocolVersion(), this.method(), this.uri(), content, this.headers().copy(), this.trailingHeaders().copy());
            dup.setDecoderResult(this.decoderResult());
            return dup;
        }

        public FullHttpRequest retain(int increment) {
            super.retain(increment);
            return this;
        }

        public FullHttpRequest retain() {
            super.retain();
            return this;
        }

        public FullHttpRequest touch() {
            super.touch();
            return this;
        }

        public FullHttpRequest touch(Object hint) {
            super.touch(hint);
            return this;
        }

        public FullHttpRequest setMethod(HttpMethod method) {
            ((HttpRequest) this.message).setMethod(method);
            return this;
        }

        public FullHttpRequest setUri(String uri) {
            ((HttpRequest) this.message).setUri(uri);
            return this;
        }

        public HttpMethod getMethod() {
            return ((HttpRequest) this.message).method();
        }

        public String getUri() {
            return ((HttpRequest) this.message).uri();
        }

        public HttpMethod method() {
            return this.getMethod();
        }

        public String uri() {
            return this.getUri();
        }

        public FullHttpRequest setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        public String toString() {
            return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
        }
    }


    private abstract static class AggregatedFullHttpMessage implements FullHttpMessage {
        protected final HttpMessage message;
        private final ByteBuf content;
        private HttpHeaders trailingHeaders;

        AggregatedFullHttpMessage(HttpMessage message, ByteBuf content, HttpHeaders trailingHeaders) {
            this.message = message;
            this.content = content;
            this.trailingHeaders = trailingHeaders;
        }

        public HttpHeaders trailingHeaders() {
            HttpHeaders trailingHeaders = this.trailingHeaders;
            return (HttpHeaders) (trailingHeaders == null ? EmptyHttpHeaders.INSTANCE : trailingHeaders);
        }

        void setTrailingHeaders(HttpHeaders trailingHeaders) {
            this.trailingHeaders = trailingHeaders;
        }

        public HttpVersion getProtocolVersion() {
            return this.message.protocolVersion();
        }

        public HttpVersion protocolVersion() {
            return this.message.protocolVersion();
        }

        public FullHttpMessage setProtocolVersion(HttpVersion version) {
            this.message.setProtocolVersion(version);
            return this;
        }

        public HttpHeaders headers() {
            return this.message.headers();
        }

        public DecoderResult decoderResult() {
            return this.message.decoderResult();
        }

        public DecoderResult getDecoderResult() {
            return this.message.decoderResult();
        }

        public void setDecoderResult(DecoderResult result) {
            this.message.setDecoderResult(result);
        }

        public ByteBuf content() {
            return this.content;
        }

        public int refCnt() {
            return this.content.refCnt();
        }

        public FullHttpMessage retain() {
            this.content.retain();
            return this;
        }

        public FullHttpMessage retain(int increment) {
            this.content.retain(increment);
            return this;
        }

        public FullHttpMessage touch(Object hint) {
            this.content.touch(hint);
            return this;
        }

        public FullHttpMessage touch() {
            this.content.touch();
            return this;
        }

        public boolean release() {
            return this.content.release();
        }

        public boolean release(int decrement) {
            return this.content.release(decrement);
        }

        public abstract FullHttpMessage copy();

        public abstract FullHttpMessage duplicate();

        public abstract FullHttpMessage retainedDuplicate();
    }

}
