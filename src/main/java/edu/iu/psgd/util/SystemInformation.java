package edu.iu.psgd.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemInformation {
    private static String hostname;

    public static String getHostname() throws UnknownHostException {
        InetAddress inetAddress;
        inetAddress = InetAddress.getLocalHost();
        hostname = inetAddress.getHostName();
        return hostname;
    }

}
