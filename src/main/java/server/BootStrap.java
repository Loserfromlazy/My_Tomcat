package server;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * MYTomcat启动主类
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/18
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



    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
            50,
            100L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(50),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * MYTomcat程序init和启动
     *
     * @author Yuhaoran
     * @date 2022/1/15 12:38
     */
    public void start() throws Exception {
        //加载web.xml
        loadServlet();
        //===================BIO
        /*ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("My_Tomcat Listening on port 8080");
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Request request = new Request(inputStream);
            Response response = new Response(outputStream);
            if (servletMap.get(request.getUrl())==null){
                response.outPutHtml(request.getUrl());
            }else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }

            socket.close();
        }*/
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
                    RequestProcess requestProcess = new RequestProcess(socketChannel,servletMap,selector);
                    threadPoolExecutor.execute(requestProcess);
                    iterator.remove();
                }
            }
        }
    }

    private Map<String,HttpServlet> servletMap = new HashMap<>();
    /**
     * 加载解析web.xml
     * @author Yuhaoran
     * @date 2022/1/18 13:18
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (Element element : selectNodes) {
                //获取servlet-name
                Node servletNameNode = element.selectSingleNode("servlet-name");
                String servletName = servletNameNode.getStringValue();
                //获取servlet-class
                Node servletClassNode = element.selectSingleNode("servlet-class");
                String servletClass = servletClassNode.getStringValue();

                //根据servlet-name找到servlet-class
                Node servletMapping = rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name = '" + servletName + "']");
                Node urlPatternNode = servletMapping.selectSingleNode("url-pattern");
                String urlPattern = urlPatternNode.getStringValue();

                //存入map
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        }catch (Exception e){
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
