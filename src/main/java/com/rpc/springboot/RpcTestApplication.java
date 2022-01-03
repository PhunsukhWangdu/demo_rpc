package com.rpc.springboot;

import com.rpc.exporter.RpcExporter;
import com.rpc.importer.RpcImporter;
import com.rpc.service.EchoService;
import com.rpc.service.EchoServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

/**
 * @author jiuwu.jl
 * @version RpcTestApplication, v 0.1 2022-01-03 18:07 jiuwu.jl Exp $
 */

@SpringBootApplication
public class RpcTestApplication {
    public static void main(String args[]) {
        SpringApplication.run(RpcTestApplication.class, args);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcExporter.exporter("localhost", 8088);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echo = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8088));
        System.out.println(echo.echo("whats the ping address ?"));
    }
}
