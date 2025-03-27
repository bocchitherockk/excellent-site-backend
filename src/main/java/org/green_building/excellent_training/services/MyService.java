package org.green_building.excellent_training.services;

import org.green_building.excellent_training.entities.Cat;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public String helloFromSpringBoot() {
	Cat cat = new Cat("yassine", 15, 3001.88, new Cat("ahmed", 22, 100.234, null));
	// return "Hello from springboot";
	return cat.toString();
    }
}
