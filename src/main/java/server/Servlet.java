package server;

/**
 * <p>
 * Servlet
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/18
 */
public interface Servlet {
    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request,Response response) throws Exception;
}
