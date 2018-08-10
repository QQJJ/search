package com.rank.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/8/10 17:18
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String toIndex(){
        return "index";
    }

}
