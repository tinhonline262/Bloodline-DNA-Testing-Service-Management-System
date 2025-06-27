package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.ServiceOrderRequest;
import com.dna_testing_system.dev.entity.MedicalService;
import com.dna_testing_system.dev.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/v1/services")
@RequiredArgsConstructor
public class ServiceDetailController {

    private final ServiceService serviceService;

    @GetMapping("/{id}")
    public String showServiceDetail(@PathVariable Long id, Model model) {
        Optional<MedicalService> service = serviceService.getServiceById(id);
        model.addAttribute("service", service.orElseGet(MedicalService::new));
        model.addAttribute("orderRequest", new ServiceOrderRequest()); // Thêm orderRequest
        return "service-detail";
    }
}