package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.entity.DnaService;
import com.dna_testing_system.dev.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/services/admin")
public class AdminServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String showServiceManagement(Model model) {
        model.addAttribute("service", new DnaService());
        model.addAttribute("services", serviceService.getAllServices());
        return "admin-services";
    }

    @PostMapping
    public String saveService(@ModelAttribute("service") DnaService dnaService, Model model) {
        try {
            serviceService.saveService(dnaService);
            model.addAttribute("success", "Lưu dịch vụ thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi lưu dịch vụ: " + e.getMessage());
        }
        model.addAttribute("service", new DnaService());
        model.addAttribute("services", serviceService.getAllServices());
        return "admin-services";
    }

    @GetMapping("/edit/{id}")
    public String editService(@PathVariable Long id, Model model) {
        DnaService service = serviceService.getServiceById(id).orElse(new DnaService());
        model.addAttribute("service", service);
        model.addAttribute("services", serviceService.getAllServices());
        return "admin-services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id, Model model) {
        try {
            serviceService.deleteService(id);
            model.addAttribute("success", "Xóa dịch vụ thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi xóa dịch vụ: " + e.getMessage());
        }
        model.addAttribute("service", new DnaService());
        model.addAttribute("services", serviceService.getAllServices());
        return "admin-services";
    }
}