package org.green_building.excellent_training.controller;

import org.green_building.excellent_training.services.MyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
// @RequestMapping("/")
// @CrossOrigin(origins = "http://localhost:6969")
public class MyController {

    // @Autowired // this is field injection which is not recommended, use a constructor injection instead like down below
    private final MyService myService;

    // note: only one Autowired constructor is allowed
    @Autowired
    public MyController(MyService myService) {
	this.myService = myService;
    }

    @GetMapping("/hello_from_springboot")
    public String getGreeting() {
	return this.myService.helloFromSpringBoot();
    }
}

