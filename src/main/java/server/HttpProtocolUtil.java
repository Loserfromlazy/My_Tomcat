package server;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * HttpProtocolUtil
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/13
 */
public class HttpProtocolUtil {

    public static String getHttpHeader200(long contentLength){
        return "HTTP/1.1 200 OK \n" +
                "Content-Type:text/html \n" +
                "Content-Length: "+contentLength+"\n" +
                "\r\n";
    }

    public static String getHttpHeader404(){
        String str404="<h1>404 not found</h1>";
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type:text/html \n" +
                "Content-Length: "+str404.getBytes(StandardCharsets.UTF_8).length+"\n" +
                "\r\n" +str404;
    }
}
