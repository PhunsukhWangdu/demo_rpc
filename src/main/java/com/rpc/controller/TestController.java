package com.rpc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author jiuwu.jl
 * @version TestController, v 0.1 2022-01-03 17:58 jiuwu.jl Exp $
 */

@RestController
public class TestController {
    @RequestMapping("/rpc")
    public void rpcHtml(HashMap<String, Object> map, Model model) {
//        model.addAttribute("s1", "test rpc from model");
//        map.put("s2", "test rpc from map")
        System.out.println("rpc");
    }
}
