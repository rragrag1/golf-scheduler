package com.golfacademy.resourceserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LessonsController {

    @GetMapping("/lessons")
    public String lessons() {
        return "List of Golf Lessons";
    }

}
