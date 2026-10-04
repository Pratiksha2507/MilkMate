package com.milkmate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/farmer")
public class RoleTestController {

    @GetMapping("/test")
    public String farmerTest() {
        return "FARMER role access successful!";
    }
}