package server;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RequestProcess
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/25
 */
public class RequestProcess implements Runnable{
    private SocketChannel socketChannel;
    private Map<String,HttpServlet> servletMap = new HashMap<>();
    private Selector selector;

    public RequestProcess(SocketChannel socketChannel, Map<String, HttpServlet> servletMap, Selector selector) {
        this.socketChannel = socketChannel;
        this.servletMap = servletMap;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println(socketChannel.getRemoteAddress()+"发来请求");
            Request request = new Request(socketChannel);
            Response response = new Response(socketChannel);
            if (servletMap.get(request.getUrl())==null){
                response.outPutHtmlByChannel(request.getUrl());
            }else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }
            socketChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
