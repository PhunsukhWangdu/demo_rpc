package com.rpc.importer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author jiuwu.jl
 * @version RpcImporter, v 0.1 2022-01-03 17:34 jiuwu.jl Exp $
 */


public class RpcImporter<S> {
    public S importer(final Class<?> serviceClass, final InetSocketAddress addr) {
        // 将本地的接口调用转化为JDK的动态代理，在动态代理中实现接口的远程调用
        return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass.getInterfaces()[0]},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 创建Socket客户端
                        Socket socket = null;
                        socket = new Socket();
                        // 根据制定的地址连接远程服务提供者
                        socket.connect(addr);

                        // 将远程服务所需要的接口类、方法名、参数列表等编码后发送给服务器提供者
                        ObjectOutputStream output = null;
                        ObjectInputStream input = null;
                        output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeUTF(serviceClass.getName());
                        output.writeUTF(method.getName());
                        output.writeObject(method.getParameterTypes());
                        output.writeObject(args);
                        // 同步阻塞等待服务端返回应答，获取应答后返回
                        input = new ObjectInputStream(socket.getInputStream());
                        return input.readObject();
                    }
                }
        );
    }
}
