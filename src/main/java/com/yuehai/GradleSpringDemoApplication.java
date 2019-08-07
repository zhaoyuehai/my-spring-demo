package com.yuehai;

import com.yuehai.util.SystemUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.net.InetAddress;
import java.net.SocketException;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class
})
@MapperScan("com.yuehai.mapper")
public class GradleSpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradleSpringDemoApplication.class, args);
        //TODO 临时把无线网局域网IP地址打印出来，方便内网调用接口
        try {
            InetAddress ipAddress = SystemUtil.getIPAddress();
            if (ipAddress != null) {
                System.out.println(" ");
                System.out.println(" ");
                System.out.println(" ");
                System.out.println(" ");
                System.out.println("****************IP****************");
                System.out.println("**********************************");
                System.out.println(" ********* " + ipAddress.getHostAddress() + " *********");
                System.out.println("**********************************");
                System.out.println("****************IP****************");
                System.out.println(" ");
                System.out.println(" ");
                System.out.println(" ");
                System.out.println(" ");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}