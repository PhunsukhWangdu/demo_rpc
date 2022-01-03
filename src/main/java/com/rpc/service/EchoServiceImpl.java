package com.rpc.service;

/**
 * @author jiuwu.jl
 * @version EchoService, v 0.1 2022-01-03 16:58 jiuwu.jl Exp $
 */


public class EchoServiceImpl implements EchoService{
    public String echo(String ping) {
        return ping != null ? ping  + " ---> ping ip address :": "ping is null";
    }
}
