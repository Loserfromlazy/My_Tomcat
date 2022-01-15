package server;

import java.io.InputStream;

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

    private InputStream inputStream;//根据传入的inputStream解析请求头

}
