package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.TestKitRequest;
import com.dna_testing_system.dev.dto.response.TestKitResponse;
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
public class TestKitController {
    TestKitService testKitService;

    @PostMapping("/test-kits/create")
    public String createTestKit(@ModelAttribute("newTestKit") TestKitRequest testKitRequest) {
        testKitService.CreateTestKit(testKitRequest);
        return "admin-manager/create-test-kit"; // Redirect to the test kits page after creation
    }

    @GetMapping("/test-kits")
    public String getTestKits(Model model) {
        List<TestKitResponse> testKits = testKitService.GetTestKitResponseList();
        model.addAttribute("testKits", testKits);
        return "admin-manager/view-test-kits"; // Return the view name for displaying test kits
    }

    @GetMapping("/test-kits/search")
    public String searchTestKits(@RequestParam("searchQuery") String kitName, Model model) {
        List<TestKitResponse> testKits = testKitService.GetTestKitResponseByName(kitName);
        model.addAttribute("testKits", testKits);
        return "admin-manager/view-test-kits"; // Return the view name for displaying search results
    }

    @GetMapping("/test-kits/update")
    public String showUpdateTestKitForm(@RequestParam("kitId") Long kitId, Model model) {
        TestKitResponse testKit = testKitService.GetTestKitResponseById(kitId);
        model.addAttribute("testKit", testKit);
        return "admin-manager/update-test-kit"; // Return the view name for the update form
    }

    @PostMapping("/test-kits/update")
    public String updateTestKit(@RequestParam("kitId") Long kitId, @ModelAttribute("testKitEdit") TestKitRequest testKitRequest) {
        testKitService.UpdateTestKit(kitId, testKitRequest);
        return "redirect:/test-kits"; // Redirect to the test kits page after updating
    }

    @GetMapping("/test-kits/delete")
    public String deleteTestKit(@RequestParam("kitId") Long kitId) {
        testKitService.DeleteTestKit(kitId);
        return "redirect:/test-kits"; // Redirect to the test kits page after deletion
    }
}
