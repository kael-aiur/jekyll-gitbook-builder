package kael.jekyll.gitbook.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import kael.jekyll.gitbook.service.JekyllAndGitBookBuilder;
import kael.jekyll.gitbook.service.WebSiteBuilder;
import kael.jekyll.gitbook.util.Properties;

import java.io.*;
import java.net.URL;

public class HttpServer {
    public void start(int port) throws Exception {

        Properties properties = new Properties(getConfig());

        properties.forEach((k,v)->System.out.println(k+":"+v));

        WebSiteBuilder builder = new JekyllAndGitBookBuilder(properties);
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            //ch.pipeline().addLast(new HttpResponseEncoder());
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            //ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65535));
                            ch.pipeline().addLast(new ServerHandler(builder,properties));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128) // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            ChannelFuture f = b.bind(port).sync(); // (7)

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private File getConfig(){
        URL url = this.getClass().getResource("/");
        File file = new File(url.getFile());
        System.out.println(url.getFile());
        File[] confs = file.listFiles((dir, name) -> name.equals("config.properties"));
        if(confs != null && confs.length > 0){
            return confs[0];
        }
        url = this.getClass().getResource("/config.properties");
        try {
            File config = new File(file.getPath()+"/config.properties");

            InputStream is = null;
            OutputStream os = null;

            try {
                is=url.openStream();
                os = new FileOutputStream(config);
                int i = is.read();
                do{
                    System.out.print((char)i);
                    os.write(i);
                    i = is.read();
                    if(i == -1){
                        break;
                    }
                }while (true);
                os.flush();
            } finally {
                if(is != null){
                    is.close();
                }
                if(os != null){
                    os.close();
                }
            }

            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
