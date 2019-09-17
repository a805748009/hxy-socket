package nafos.bootStrap.handle.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;


public class BuildHttpObjectAggregator extends HttpObjectAggregator {
    public BuildHttpObjectAggregator(int maxContentLength) {
        super(maxContentLength);
    }

    public BuildHttpObjectAggregator(int maxContentLength, boolean closeOnExpectationFailed) {
        super(maxContentLength, closeOnExpectationFailed);
    }

    @Override
    protected void aggregate(FullHttpMessage aggregated, HttpContent content) throws Exception {
        if (content instanceof LastHttpContent) {
            ((NsRequest) aggregated).setTrailingHeaders(((LastHttpContent) content).trailingHeaders());
        }

    }

    @Override
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

            ret = new AggregatedFullHttpResponse((HttpResponse) start, content, null);
        }

        return (FullHttpMessage) ret;
    }

    private static final class AggregatedFullHttpResponse extends AggregatedFullHttpMessage implements FullHttpResponse {
        AggregatedFullHttpResponse(HttpResponse message, ByteBuf content, HttpHeaders trailingHeaders) {
            super(message, content, trailingHeaders);
        }

        @Override
        public FullHttpResponse copy() {
            return this.replace(this.content().copy());
        }

        @Override
        public FullHttpResponse duplicate() {
            return this.replace(this.content().duplicate());
        }

        @Override
        public FullHttpResponse retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }

        @Override
        public FullHttpResponse replace(ByteBuf content) {
            DefaultFullHttpResponse dup = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), content, this.headers().copy(), this.trailingHeaders().copy());
            dup.setDecoderResult(this.decoderResult());
            return dup;
        }

        @Override
        public FullHttpResponse setStatus(HttpResponseStatus status) {
            ((HttpResponse) this.message).setStatus(status);
            return this;
        }

        @Override
        public HttpResponseStatus getStatus() {
            return ((HttpResponse) this.message).status();
        }

        @Override
        public HttpResponseStatus status() {
            return this.getStatus();
        }

        @Override
        public FullHttpResponse setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        @Override
        public FullHttpResponse retain(int increment) {
            super.retain(increment);
            return this;
        }

        @Override
        public FullHttpResponse retain() {
            super.retain();
            return this;
        }

        @Override
        public FullHttpResponse touch(Object hint) {
            super.touch(hint);
            return this;
        }

        @Override
        public FullHttpResponse touch() {
            super.touch();
            return this;
        }

        @Override
        public String toString() {
            return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
        }


    }

    public static class AggregatedFullHttpRequest extends AggregatedFullHttpMessage implements FullHttpRequest {
        public AggregatedFullHttpRequest(HttpRequest request, ByteBuf content, HttpHeaders trailingHeaders) {
            super(request, content, trailingHeaders);
        }

        @Override
        public FullHttpRequest copy() {
            return this.replace(this.content().copy());
        }

        @Override
        public FullHttpRequest duplicate() {
            return this.replace(this.content().duplicate());
        }

        @Override
        public FullHttpRequest retainedDuplicate() {
            return this.replace(this.content().retainedDuplicate());
        }

        @Override
        public FullHttpRequest replace(ByteBuf content) {
            DefaultFullHttpRequest dup = new DefaultFullHttpRequest(this.protocolVersion(), this.method(), this.uri(), content, this.headers().copy(), this.trailingHeaders().copy());
            dup.setDecoderResult(this.decoderResult());
            return dup;
        }

        @Override
        public FullHttpRequest retain(int increment) {
            super.retain(increment);
            return this;
        }

        @Override
        public FullHttpRequest retain() {
            super.retain();
            return this;
        }

        @Override
        public FullHttpRequest touch() {
            super.touch();
            return this;
        }

        @Override
        public FullHttpRequest touch(Object hint) {
            super.touch(hint);
            return this;
        }

        @Override
        public FullHttpRequest setMethod(HttpMethod method) {
            ((HttpRequest) this.message).setMethod(method);
            return this;
        }

        @Override
        public FullHttpRequest setUri(String uri) {
            ((HttpRequest) this.message).setUri(uri);
            return this;
        }

        @Override
        public HttpMethod getMethod() {
            return ((HttpRequest) this.message).method();
        }

        @Override
        public String getUri() {
            return ((HttpRequest) this.message).uri();
        }

        @Override
        public HttpMethod method() {
            return this.getMethod();
        }

        @Override
        public String uri() {
            return this.getUri();
        }

        @Override
        public FullHttpRequest setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        @Override
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

        @Override
        public HttpHeaders trailingHeaders() {
            HttpHeaders trailingHeaders = this.trailingHeaders;
            return (HttpHeaders) (trailingHeaders == null ? EmptyHttpHeaders.INSTANCE : trailingHeaders);
        }

        void setTrailingHeaders(HttpHeaders trailingHeaders) {
            this.trailingHeaders = trailingHeaders;
        }

        @Override
        public HttpVersion getProtocolVersion() {
            return this.message.protocolVersion();
        }

        @Override
        public HttpVersion protocolVersion() {
            return this.message.protocolVersion();
        }

        @Override
        public FullHttpMessage setProtocolVersion(HttpVersion version) {
            this.message.setProtocolVersion(version);
            return this;
        }

        @Override
        public HttpHeaders headers() {
            return this.message.headers();
        }

        @Override
        public DecoderResult decoderResult() {
            return this.message.decoderResult();
        }

        @Override
        public DecoderResult getDecoderResult() {
            return this.message.decoderResult();
        }

        @Override
        public void setDecoderResult(DecoderResult result) {
            this.message.setDecoderResult(result);
        }

        @Override
        public ByteBuf content() {
            return this.content;
        }

        @Override
        public int refCnt() {
            return this.content.refCnt();
        }

        @Override
        public FullHttpMessage retain() {
            this.content.retain();
            return this;
        }

        @Override
        public FullHttpMessage retain(int increment) {
            this.content.retain(increment);
            return this;
        }

        @Override
        public FullHttpMessage touch(Object hint) {
            this.content.touch(hint);
            return this;
        }

        @Override
        public FullHttpMessage touch() {
            this.content.touch();
            return this;
        }

        @Override
        public boolean release() {
            return this.content.release();
        }

        @Override
        public boolean release(int decrement) {
            return this.content.release(decrement);
        }

        @Override
        public abstract FullHttpMessage copy();

        @Override
        public abstract FullHttpMessage duplicate();

        @Override
        public abstract FullHttpMessage retainedDuplicate();
    }

}
