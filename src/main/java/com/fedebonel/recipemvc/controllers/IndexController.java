package com.fedebonel.recipemvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller of the index page
 */
@Controller
@RequestMapping("")
public class IndexController {
    /**
     * When a http get request gets done to the server under the localhost:8080 endpoint
     * return the index page
     */
    @RequestMapping({"index", "index.html"})
    public String getIndexPage(){
        return "index";
    }
}
