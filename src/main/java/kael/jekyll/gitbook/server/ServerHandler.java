package kael.jekyll.gitbook.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import kael.jekyll.gitbook.service.JekyllAndGitBookBuilder;
import kael.jekyll.gitbook.service.WebSiteBuilder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final WebSiteBuilder builder;
    private static final String hookHeader = "X-Gitlab-Token";

    public ServerHandler(WebSiteBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest)msg;
            request.headers().forEach(stringStringEntry -> System.out.println(stringStringEntry.getKey()+":"+stringStringEntry.getValue()));
            String secret = request.headers().get(hookHeader);
            if("kaelkaelkael".equals(secret)){
                ByteBuf buf = request.content();

                StringReader sr = new StringReader(buf.toString(io.netty.util.CharsetUtil.UTF_8));
                JsonReader reader = Json.createReader(sr);
                JsonObject json = reader.readObject();

                Map<String, Object> params = new HashMap<>(json.size());
                json.forEach((k, v) -> params.put(k,v));
                builder.build(params);
            }
        }

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.wrappedBuffer("build success".getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}
