package server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * MySevlet
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/18
 */
public class MyServlet extends HttpServlet{
    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>Hello My_Tomcat GET Method!</h1>";
        try {
            //BIO
            //response.outPut(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length)+content);
            //NIO
            response.outPutByChannel(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>Hello My_Tomcat POST Method!</h1>";
        try {
            //BIO
            //response.outPut(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length)+content);
            //NIO
            response.outPutByChannel(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
