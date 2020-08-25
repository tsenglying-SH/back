package com.shu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author Tsenglying
 * @Date 2020/8/24 17:01
 * @Version 1.0
 **/
@RestController
public class HelloController {
    @RequestMapping("/quick")
    public String HelloTest() {
        return "hello";
    }
}
