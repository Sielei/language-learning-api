package com.hs.languagelearningapi.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/redoc")
public class RedocController {

    @GetMapping
    public String showRedoc(Model model){
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v3/api-docs")
                .build().toUri();
        model.addAttribute("location", location);
        return "redoc";
    }
}
