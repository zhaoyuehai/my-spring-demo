package com.yuehai.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by zhaoyuehai 2019/4/3
 */
public class SystemUtil {

    public static InetAddress getIPAddress() throws SocketException {
        // 获得本机的所有网络接口
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            NetworkInterface nif = nifs.nextElement();
            if (nif.getName().startsWith("wlan")) {
                Enumeration<InetAddress> addresses = nif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.getAddress().length == 4) { // 速度快于 instanceof
                        return addr;
                    }
                }
            }
        }
        return null;
    }
//    private static void getIPAddress() throws SocketException {
//         获得本机的所有网络接口
//        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
//        while (nifs.hasMoreElements()) {
//            NetworkInterface nif = nifs.nextElement();
//             获得与该网络接口绑定的 IP 地址，一般只有一个
//            Enumeration<InetAddress> addresses = nif.getInetAddresses();
//            while (addresses.hasMoreElements()) {
//                InetAddress addr = addresses.nextElement();
//                if (addr instanceof Inet4Address) { // 只关心 IPv4 地址
//                    if (!addr.isLoopbackAddress()) {// 排除loopback类型地址:只要第一个字节是127，就是lookback地址
//                        if (!addr.isLinkLocalAddress()) {//排除LinkLocalAddress 本地连接地址 169.254.0.0 ~ 169.254.255.255
//                            System.out.println("网卡接口名称：" + nif.getName());
//                            System.out.println("网卡接口地址：" + addr.getHostAddress());
//                            System.out.println();
//                        }
//                    }
//                }
//            }
//        }
//    }
}
