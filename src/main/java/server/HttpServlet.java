package server;

/**
 * <p>
 * HttpServlet
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/18
 */
public abstract class HttpServlet implements Servlet{
    public abstract void doGet(Request request,Response response);

    public abstract void doPost(Request request,Response response);

    @Override
    public void service(Request request,Response response) throws Exception {
        if ("GET".equals(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }
}
