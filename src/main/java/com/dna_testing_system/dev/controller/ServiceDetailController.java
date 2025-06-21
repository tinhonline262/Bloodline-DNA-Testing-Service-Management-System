package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.entity.DnaService;
import com.dna_testing_system.dev.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/v1/services")
public class ServiceDetailController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/{id}")
    public String showServiceDetail(@PathVariable Long id, Model model) {
        Optional<DnaService> service = serviceService.getServiceById(id);
        model.addAttribute("service", service.orElse(new DnaService()));
        return "service-detail";
    }
}