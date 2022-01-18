package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * StaticResourceUtil
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/1/17
 */
public class StaticResourceUtil {

    public static String getAbsolutePath(String path) {
        String resourcePath = StaticResourceUtil.class.getResource("/").getPath();
        return resourcePath.replaceAll("\\\\", "/") + path;
    }
}
