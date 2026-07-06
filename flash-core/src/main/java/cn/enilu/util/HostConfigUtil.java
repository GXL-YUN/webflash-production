package cn.enilu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HostConfigUtil {

    @Value("${hostUrlNginx}")
    private String hostUrlNginx;

    public String getHostProt() {
        return hostUrlNginx;
    }
}