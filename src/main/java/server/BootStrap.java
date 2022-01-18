package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * <p>
 * MYTomcat启动主类
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/12
 */
public class BootStrap {
    //暂时写死，可以修改到xml文件中进行读取
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * MYTomcat程序init和启动
     *
     * @author Yuhaoran
     * @date 2022/1/15 12:38
     */
    public void start() throws IOException {
        //===================BIO
//        ServerSocket serverSocket = new ServerSocket(port);
//        System.out.println("My_Tomcat Listening on port 8080");
//        while (true) {
//            Socket socket = serverSocket.accept();
//            InputStream inputStream = socket.getInputStream();
//            OutputStream outputStream = socket.getOutputStream();
//            Request request = new Request(inputStream);
//            Response response = new Response(outputStream);
//            response.outPutHtml(request.getUrl());
//            socket.close();
//        }
        //==================NIO
        ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        System.out.println("My_Tomcat Listening on port 8080");
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            if (selector.select(2000)==0){
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if (key.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    System.out.println(socketChannel.getRemoteAddress()+"发来请求");
                    Request request = new Request(socketChannel);
                    Response response = new Response(socketChannel);
                    response.outPutHtmlByChannel(request.getUrl());
                    socketChannel.close();
                    iterator.remove();
                }
            }
        }
    }


    /**
     * MYTomcat程序入口
     *
     * @author Yuhaoran
     * @date 2022/1/12 15:24
     */
    public static void main(String[] args) {
        BootStrap bootStrap = new BootStrap();
        try {
            bootStrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
