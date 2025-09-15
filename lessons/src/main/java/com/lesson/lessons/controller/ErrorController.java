package com.lesson.lessons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//this is a controller to hold users who were redirected here after denail of access
//due to a not high enough role profile
public class ErrorController {

    //returns a simple string
    @GetMapping("/access-denied")
    @ResponseBody
    public String accessDenied() {
        return "Access Denied: You do not have permission to view this page.";
    }
}
