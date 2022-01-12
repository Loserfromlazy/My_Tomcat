package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
     * @date 2022/1/12 15:35
     */
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        while (true){
            Socket socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("Hello,World!".getBytes(StandardCharsets.UTF_8));
            socket.close();
        }
    }


    /**
     * MYTomcat程序入口
     *
     * @author Yuhaoran
     * @date 2022/1/12 15:24
     */
    public static void main(String[] args) {

    }
}
