package com.rpc.exporter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author jiuwu.jl
 * @version RpcExporter, v 0.1 2022-01-03 17:03 jiuwu.jl Exp $
 */


public class RpcExporter {
    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static class ExporterTask implements Runnable {
        Socket client = null;
        public ExporterTask(Socket client) {
            this.client = client;
        }
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {
                // 将客户端发送的码流反序列化成对象
                input = new ObjectInputStream(client.getInputStream());
                String interfaceName = input.readUTF();
                Class<?> service = Class.forName(interfaceName);
                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                Method methhod = service.getMethod(methodName, parameterTypes);
                // 反射调用服务实现者，获取执行结果
                Object result = methhod.invoke(service.newInstance(), arguments);
                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 远程服务调用完成之后，释放Socket等连接资源，防止句柄泄漏
                if(output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void exporter(String hostName, int port) throws Exception {
        // 监听TCP连接
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(hostName, port));
        try{
            while (true) {
                // 接收到客户端连接之后，将其封装成ExporterTask，由线程池执行
                executor.execute(new ExporterTask(server.accept()));
            }
        } finally {
            server.close();
        }
    }
}

