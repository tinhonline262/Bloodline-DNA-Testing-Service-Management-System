package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.SampleCollection;
import com.dna_testing_system.dev.entity.ServiceOrder;
import com.dna_testing_system.dev.entity.TestResult;
import com.dna_testing_system.dev.entity.User;

public interface EmailSender {
    boolean sendNewOrderNotificationToManager(ServiceOrder order, String managerEmail);
    boolean sendTestAssignmentNotification(ServiceOrder order, SampleCollection sampleCollection, User staffMember);
    boolean sendTestAssignmentNotification(ServiceOrder order, TestResult testResult, User staffMember);
    boolean sendOrderStatusUpdateNotification(ServiceOrder order, String recipient);
    boolean sendResultAvailableNotification(TestResult testResult, String recipient);
}