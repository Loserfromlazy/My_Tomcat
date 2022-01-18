package server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <p>
 * Request
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/15
 */
public class Request {

    private String method;//例如GET POST
    private String url;//例如 /index.html

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private InputStream inputStream;//根据传入的inputStream解析请求头
    private SocketChannel socketChannel;////根据传入的SocketChannel解析请求头(NIO)

    public Request() {
    }

    public Request(SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = 0;
        while (count==0){
            count = socketChannel.read(buffer);
        }
        buffer.flip();
        String httpHeaderStr = new String(buffer.array());//http头协议
        String[] split = httpHeaderStr.split("\\n");
        String firstLine = split[0];//协议第一行
        String[] firstLineItem = firstLine.split(" ");
        this.method =firstLineItem[0];
        this.url = firstLineItem[1];
        System.out.println("method==>"+this.method+";url==>"+this.url);
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte [] bytes = new byte[count];
        inputStream.read(bytes);
        String httpHeaderStr = new String(bytes);//http头协议
        String[] split = httpHeaderStr.split("\\n");
        String firstLine = split[0];//协议第一行
        String[] firstLineItem = firstLine.split(" ");
        this.method =firstLineItem[0];
        this.url = firstLineItem[1];
        System.out.println("method==>"+this.method+";url==>"+this.url);

    }
}
