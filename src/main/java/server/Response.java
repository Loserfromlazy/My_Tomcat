package server;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * Response
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/17
 */
public class Response {

    private OutputStream outputStream;

    private SocketChannel socketChannel;

    public Response(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 通过url获取静态资源的绝对路径，根据绝对路径获取静态文件，然后使用输出流输出
     *
     * @author Yuhaoran
     * @date 2022/1/17 13:23
     */
    public void outPutHtml(String url) throws IOException {
        String absolutePath = StaticResourceUtil.getAbsolutePath(url);
        File file = new File(absolutePath);
        FileInputStream inputStream = new FileInputStream(file);
        if (file.exists() && file.isFile()){
            //输出文件内容
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }
            int written = 0;
            int byteSize = 1024;
            byte[] bytes = new byte[byteSize];
            outputStream.write(HttpProtocolUtil.getHttpHeader200(count).getBytes(StandardCharsets.UTF_8));
            while (written < count) {
                if (written + byteSize > count) {
                    bytes = new byte[count - written];
                }
                inputStream.read(bytes);
                outputStream.write(bytes);
                outputStream.flush();
                written += byteSize;
            }
        }else {
            //输出404
            outputStream.write(HttpProtocolUtil.getHttpHeader404().getBytes(StandardCharsets.UTF_8));
        }
    }

    public void outPutHtmlByChannel(String url) throws IOException {
        String absolutePath = StaticResourceUtil.getAbsolutePath(url);
        File file = new File(absolutePath);
        if (file.exists() && file.isFile()){
            //输出文件内容
            FileInputStream inputStream = new FileInputStream(file);
            FileChannel fileChannel = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
            fileChannel.read(buffer);
            buffer.flip();
            byte[] bytes = HttpProtocolUtil.getHttpHeader200(buffer.capacity()).getBytes(StandardCharsets.UTF_8);
            ByteBuffer responseBuffer = ByteBuffer.wrap(bytes);
            socketChannel.write(responseBuffer);
            socketChannel.write(buffer);
        }else {
            //输出404
            byte[] bytes = HttpProtocolUtil.getHttpHeader404().getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            socketChannel.write(buffer);
        }
    }
}
