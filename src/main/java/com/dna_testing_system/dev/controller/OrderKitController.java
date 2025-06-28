package com.dna_testing_system.dev.controller;


import com.dna_testing_system.dev.dto.request.OrderTestKitRequest;
import com.dna_testing_system.dev.dto.response.TestKitResponse;
import com.dna_testing_system.dev.service.OrderKitService;
import com.dna_testing_system.dev.service.TestKitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderKitController {
    TestKitService testKitService;
    OrderKitService orderKitService;

    @GetMapping("/list-kit")
    public String listKits(Model model) {
        List<TestKitResponse> testKitResponse = testKitService.GetTestKitResponseList();
        model.addAttribute("testKitResponse", testKitResponse);
        return "OrderKit/list-kit"; // Assuming you have a Thymeleaf template named "list-kits.html"
    }

    @GetMapping("/order-kit")
    public String orderKit(@RequestParam("kitTestId") Long kitTestId, Model model) {
        OrderTestKitRequest request = new OrderTestKitRequest();
        request.setKitTestId(kitTestId);
        model.addAttribute("orderTestKitRequest", request);
        return "OrderKit/order-kit"; // Assuming you have a Thymeleaf template named "order-kit.html"
    }

    @PostMapping("/order-kit")
    public String submitOrder(@ModelAttribute("orderTestKitRequest") OrderTestKitRequest orderTestKitRequest, Model model) {
        orderKitService.createOrder(orderTestKitRequest);
        model.addAttribute("message", "Order placed successfully!");
        return "redirect:/list-kit"; // Redirect to the list of kits after successful order
    }


}
