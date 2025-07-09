package com.dna_testing_system.dev.controller;

import com.dna_testing_system.dev.dto.request.RawDataRequest;
import com.dna_testing_system.dev.dto.request.TestResultsResquest;
import com.dna_testing_system.dev.dto.response.*;
import com.dna_testing_system.dev.entity.RawTestData;
import com.dna_testing_system.dev.entity.TestResult;
import com.dna_testing_system.dev.enums.ServiceOrderStatus;
import com.dna_testing_system.dev.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/staff")
public class CRUDstaffController {
    OrderService orderService;
    OrderKitService orderKitService;
    OrderParticipantService orderParticipantService;
    StaffService staffService;
    OrderTaskManagementService orderTaskManagementService;
    FileEdit fileEdit;
    @GetMapping("/list-orders")
    public String listOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<CRUDorderResponse> serviceOrders = staffService.getForStaff(currentPrincipalName);
        model.addAttribute("serviceOrders", serviceOrders);
        return "staff/list-orders"; // Assuming you have a Thymeleaf template named "list-orders.html"
    }

    @GetMapping("/details-order")
    public String detailsOrder(@RequestParam("orderId") Long orderId ,Model model) {
        CRUDorderResponse serviceOrder = staffService.getOrderById(orderId);
        List<OrderTestKitResponse> orderTestKits = orderKitService.getOrderById(orderId);
        List<OrderParticipantResponse> orderParticipants = orderParticipantService.getAllParticipantsByOrderId(orderId);
        model.addAttribute("orderTestKits", orderTestKits);
        model.addAttribute("orderParticipants", orderParticipants);
        model.addAttribute("serviceOrderByCustomerResponse", serviceOrder);
        return "staff/details-order"; // Assuming you have a Thymeleaf template named "details-order.html"
    }

    @GetMapping("/update-order")
    public String viewUpdateOrder(@RequestParam("orderId") Long orderId ,Model model) {
        CRUDorderResponse serviceOrder = staffService.getOrderById(orderId);
        List<OrderTestKitResponse> orderTestKits = orderKitService.getOrderById(orderId);
        List<OrderParticipantResponse> orderParticipants = orderParticipantService.getAllParticipantsByOrderId(orderId);
        model.addAttribute("orderTestKits", orderTestKits);
        model.addAttribute("orderParticipants", orderParticipants);
        model.addAttribute("serviceOrderByCustomerResponse", serviceOrder);
        return "staff/update-order"; // Assuming you have a Thymeleaf template named "details-order.html"
    }
    @PostMapping("/update-order")
    public String updateOrder(@RequestParam("orderId") Long orderId, @RequestParam("status") String status) {
        orderTaskManagementService.updateOrderStatus(orderId,status);
        return "redirect:/staff/list-orders"; // Redirect to the list of orders after updating
    }
    // create raw data
    @GetMapping("/create-raw-data")
    public String viewCreateRawData(@RequestParam("testResultsId") Long testResultsId, Model model) {
        model.addAttribute("rawData", new RawDataRequest());
        model.addAttribute("today", LocalDateTime.now());
        model.addAttribute("testResultsId", testResultsId); // truyền vào model
        return "staff/create-raw-data";
    }
    @PostMapping("/create-raw-data")
    public String createRawData(@RequestParam("filePath") MultipartFile filePath,
                                @ModelAttribute("rawData") RawDataRequest rawDataRequest,
                                @RequestParam("testResultsId") Long testResultsId) {
        if (filePath != null && !filePath.isEmpty()) {
            rawDataRequest.setFile(fileEdit.editFile(filePath));
        }
        staffService.createRawData(rawDataRequest,testResultsId);
        return "redirect:/staff/list-test-results"; // Redirect to the list of test results after creating
    }
    // end update order status
    @GetMapping("/list-sample-collection")
    public String listSampleCollection(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<CRUDsampleCollectionResponse> sampleCollections = staffService.getSampleCollectionTasks(currentPrincipalName);
        model.addAttribute("sampleCollections", sampleCollections);
        return "staff/list-sample-collection";
    }


    @GetMapping("/details-sample-collection")
    public String detailsSampleCollection(@RequestParam("sampleCollectionId") Long sampleCollectionId, Model model) {
        CRUDsampleCollectionResponse sampleCollection = staffService.getSampleCollectionTasksById(sampleCollectionId);
        model.addAttribute("sampleCollection", sampleCollection);
        return "staff/details-sample-collection"; // Assuming you have a Thymeleaf template named "details-sample-collection.html"
    }


    @GetMapping("/update-sample-collection")
    public String viewUpdateSampleCollection(@RequestParam("sampleCollectionId") Long sampleCollectionId, Model model) {
        CRUDsampleCollectionResponse sampleCollection = staffService.getSampleCollectionTasksById(sampleCollectionId);
        model.addAttribute("sampleCollection", sampleCollection);
        return "staff/update-sample-collection";
    }
    @PostMapping("/update-sample-collection")
    public String updateSampleCollection(
            @RequestParam("sampleCollectionId") Long sampleCollectionId,
            @RequestParam("collectionStatus") String collectionStatus,
            @RequestParam("sampleQuality") String sampleQuality,
            RedirectAttributes redirectAttributes) {

        try {
            // Validate input
            if (sampleCollectionId == null || collectionStatus == null || sampleQuality == null) {
                throw new IllegalArgumentException("Missing required parameters");
            }

            staffService.updateSampleCollectionStatus(sampleCollectionId, collectionStatus, sampleQuality);
            redirectAttributes.addFlashAttribute("successMessage", "Sample collection updated successfully!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid input: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update: " + e.getMessage());
        }

        return "redirect:/staff/list-sample-collection";
    }

    @GetMapping("/list-test-results")
    public String listTestResults(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<TestResultsResponse> testResults = staffService.getTestResults(currentPrincipalName);
        model.addAttribute("testResults", testResults);
        return "staff/list-test-results"; // Assuming you have a Thymeleaf template named "list-test-results.html"
    }

    @GetMapping("/details-test-result")
    public String detailsTestResult(@RequestParam("testResultId") Long testResultId, Model model) {
        TestResultsResponse testResult = staffService.getTestResultById(testResultId);
        RawDataResponse rawData = staffService.getRawDataById(testResult.getRawTestId());
        model.addAttribute("testResult", testResult);
        model.addAttribute("rawData", rawData);
        return "staff/details-test-result"; // Assuming you have a Thymeleaf template named "details-test-result.html"
    }

    @GetMapping("/update-test-result")
    public String viewUpdateTestResult(@RequestParam("testResultId") Long testResultId, Model model) {
        TestResultsResponse testResult = staffService.getTestResultById(testResultId);
        model.addAttribute("testResult", testResult);
        return "staff/update-test-result"; // Assuming you have a Thymeleaf template named "update-test-result.html"
    }

    @PostMapping("/update-test-result")
    public String updateTestResult(@RequestParam("testResultId") Long testResultId,
                                   @RequestParam("reportFilePath") MultipartFile reportFilePath,
                                   @ModelAttribute("testResultEdit") TestResultsResquest testResultsResquest
                                   ) {
        TestResultsResponse existingTestResult = staffService.getTestResultById(testResultId);
        if( reportFilePath == null || reportFilePath.isEmpty() && existingTestResult.getReportFile() != null) {
            testResultsResquest.setReportFile(existingTestResult.getReportFile());
        }
        else{
            if (existingTestResult.getReportFile() == null ) {
                testResultsResquest.setReportFile(fileEdit.editFile(reportFilePath));
            }
            else {
                fileEdit.deleteFile(existingTestResult.getReportFile());
                testResultsResquest.setReportFile(fileEdit.editFile(reportFilePath));
            }
        }
        if (testResultsResquest.getTestDate() == null) {
            testResultsResquest.setTestDate(existingTestResult.getTestDate()); // Set current date if null
        }
        testResultsResquest.setReportGenerated(true);
        staffService.updateTestResult(testResultsResquest, testResultId);
        return "redirect:/staff/list-test-results"; // Redirect to the list of test results after updating
    }
    // end update test result
    // update raw data
    @GetMapping("/update-raw-data")
    public String listRawData(@RequestParam("testResultId") Long testResultId,Model model) {
        TestResultsResponse testResult = staffService.getTestResultById(testResultId);
        RawDataResponse rawData = staffService.getRawDataById(testResult.getRawTestId());
        model.addAttribute("rawData", rawData);
        model.addAttribute("today", LocalDateTime.now());
        return "staff/update-raw-data"; // Assuming you have a Thymeleaf template named "list-raw-data.html"
    }

    @PostMapping("/update-raw-data")
    public String updateRawData(@RequestParam("rawDataId") Long rawDataId,
                                @RequestParam("filePath") MultipartFile filePath,
                                @ModelAttribute("rawDataEdit") RawDataRequest rawDataRequest) {
        RawDataResponse existingRawData = staffService.getRawDataById(rawDataId);
        if (filePath == null || filePath.isEmpty() && existingRawData.getFilePath() != null) {
            rawDataRequest.setFile(existingRawData.getFilePath());
        }
        else {
            if (existingRawData.getFilePath() == null) {
                rawDataRequest.setFile(fileEdit.editFile(filePath));
            } else {
                fileEdit.deleteFile(existingRawData.getFilePath());
                rawDataRequest.setFile(fileEdit.editFile(filePath));
            }
        }
        if( rawDataRequest.getCollectionDate() == null) {
            rawDataRequest.setCollectionDate(existingRawData.getCollectedAt()); // Set current date if null
        }
        staffService.updateRawData(rawDataRequest, rawDataId);
        return "redirect:/staff/list-test-results"; // Redirect to the list of test results after updating
    }
    // end update raw data

    @GetMapping("/search-orders")
    public String searchOrders(@RequestParam("query") Long query, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        CRUDorderResponse serviceOrders = staffService.searchOrderById(query, currentPrincipalName);
        if(serviceOrders == null) {
            model.addAttribute("errorMessage", "No order found with ID: " + query);
            return "redirect:/staff/list-orders"; // Redirect to the list of orders if no order is found
        }
        model.addAttribute("serviceOrders", serviceOrders);
        return "staff/list-orders"; // Assuming you have a Thymeleaf template named "list-orders.html"
    }

    @GetMapping("/search-test-results")
    public String searchTestResults(@RequestParam("query") Long query, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        TestResultsResponse testResults = staffService.searchTestResultById(query, currentPrincipalName);
        if(testResults == null) {
            model.addAttribute("errorMessage", "No test result found with ID: " + query);
            return "redirect:/staff/list-test-results"; // Redirect to the list of test results if no result is found
        }
        model.addAttribute("testResults", testResults);
        return "staff/list-test-results"; // Assuming you have a Thymeleaf template named "list-test-results.html"
    }

    @GetMapping("/search-sample-collections")
    public String searchSampleCollections(@RequestParam("query") Long query, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        CRUDsampleCollectionResponse sampleCollections = staffService.searchSampleCollectionById(query, currentPrincipalName);
        if(sampleCollections == null) {
            model.addAttribute("errorMessage", "No sample collection found with ID: " + query);
            return "redirect:/staff/list-sample-collection"; // Redirect to the list of sample collections if no collection is found
        }
        model.addAttribute("sampleCollections", sampleCollections);
        return "staff/list-sample-collection"; // Assuming you have a Thymeleaf template named "list-sample-collection.html"
    }

    @GetMapping("/filter-orders")
    public String filterOrders(@RequestParam("status") String status, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<CRUDorderResponse> filteredOrders = staffService.filterOrdersByStatus(status, currentPrincipalName);
        if(filteredOrders == null) {
            model.addAttribute("errorMessage", "No orders found with status: " + status);
            return "redirect:/staff/list-orders"; // Redirect to the list of orders if no orders are found
        }
        model.addAttribute("serviceOrders", filteredOrders);
        return "staff/list-orders"; // Assuming you have a Thymeleaf template named "list-orders.html"
    }

    @GetMapping("/filter-sample-collections")
    public String filterSampleCollections(@RequestParam("status") String status, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<CRUDsampleCollectionResponse> filteredSampleCollections = staffService.filterSampleCollectionsByStatus(status, currentPrincipalName);
        model.addAttribute("sampleCollections", filteredSampleCollections);
        return "staff/list-sample-collection"; // Assuming you have a Thymeleaf template named "list-sample-collection.html"
    }

    @GetMapping("/filter-test-results")
    public String filterTestResults(@RequestParam("status") String status, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<TestResultsResponse> filteredTestResults = staffService.filterTestResultsByStatus(status, currentPrincipalName);
        model.addAttribute("testResults", filteredTestResults);
        return "staff/list-test-results"; // Assuming you have a Thymeleaf template named "list-test-results.html"
    }


    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "section", required = false) String section, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if ("orders".equals(section)) {
            List<CRUDorderResponse> serviceOrders = staffService.getForStaff(username);
            model.addAttribute("serviceOrders", serviceOrders);
        } else if ("sample-collection".equals(section)) {
            List<CRUDsampleCollectionResponse> sampleCollections = staffService.getSampleCollectionTasks(username);
            model.addAttribute("sampleCollections", sampleCollections);
        } else if ("test-results".equals(section)) {
            List<TestResultsResponse> testResults = staffService.getTestResults(username);
            model.addAttribute("testResults", testResults);
        }

        model.addAttribute("section", section);
        return "staff/dashboard";
    }
}
