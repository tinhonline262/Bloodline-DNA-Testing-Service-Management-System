package com.dna_testing_system.dev.service.impl;

import com.dna_testing_system.dev.dto.request.OrderTestKitRequest;
import com.dna_testing_system.dev.dto.response.OrderTestKitResponse;
import com.dna_testing_system.dev.entity.OrderKit;
import com.dna_testing_system.dev.entity.TestKit;
import com.dna_testing_system.dev.entity.User;
import com.dna_testing_system.dev.mapper.OrderTestKitMapper;
import com.dna_testing_system.dev.repository.OrderTestKitRepository;
import com.dna_testing_system.dev.repository.TestKitRepository;
import com.dna_testing_system.dev.repository.UserRepository;
import com.dna_testing_system.dev.service.OrderKitService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderKitServiceImpl implements OrderKitService {
    OrderTestKitMapper orderTestKitMapper;
    OrderTestKitRepository orderKitRepository;
    TestKitRepository testKitRepository;

    @Override
    @Transactional
    public void createOrder(OrderTestKitRequest orderTestKitRequest) {
        OrderKit orderKit = orderTestKitMapper.toOrderKit(orderTestKitRequest);
        TestKit testKit = testKitRepository.getOne(orderKit.getId());
        if (testKit == null) {
            throw new IllegalArgumentException("Test kit not found for ID: " + orderKit.getId());
        }
        orderKit.setUnitPrice(testKit.getCurrentPrice());
        orderKit.setTotalPrice(orderKit.getUnitPrice().multiply(BigDecimal.valueOf(orderKit.getQuantityOrdered().longValue())));
        // Save the orderKit to the database (repository save method would be called here)
        orderKitRepository.save(orderKit);
        // Optionally, you might want to update the stock of the kit after ordering
        testKit.setQuantityInStock(
                testKit.getQuantityInStock() - orderKit.getQuantityOrdered());
        testKitRepository.save(testKit);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderKitRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public void updateOrder(Long orderId, OrderTestKitRequest orderTestKitRequest) {
        OrderKit orderKit = orderTestKitMapper.toOrderKit(orderTestKitRequest);
        TestKit testKit = testKitRepository.getOne(orderKit.getId());
        if (testKit == null) {
            throw new IllegalArgumentException("Test kit not found for ID: " + orderKit.getId());
        }
        if (orderTestKitRequest.getQuantityOrdered() < orderKit.getQuantityOrdered()) {
            // If the new quantity is less than the current quantity, we need to update the stock
            testKit.setQuantityInStock(testKit.getQuantityInStock() + (orderKit.getQuantityOrdered() - orderTestKitRequest.getQuantityOrdered()));

        } else {
            // If the new quantity is greater, we need to reduce the stock accordingly
            testKit.setQuantityInStock(testKit.getQuantityInStock() - (orderTestKitRequest.getQuantityOrdered() - orderKit.getQuantityOrdered()));
        }
        orderKit.setUnitPrice(testKit.getCurrentPrice());
        orderKit.setTotalPrice(orderKit.getUnitPrice().multiply(BigDecimal.valueOf(orderTestKitRequest.getQuantityOrdered().longValue())));
        // Update the orderKit in the database
        orderKitRepository.save(orderKit);
        // Update the test kit stock in the database
        testKitRepository.save(testKit);
    }

    @Override
    public OrderTestKitResponse getOrderById(Long orderId) {
        OrderKit orderKit = orderKitRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for ID: " + orderId));
        return orderTestKitMapper.toOrderTestKitResponse(orderKit);
    }

    @Override
    public List<OrderTestKitResponse> getAllOrders() {
        List<OrderKit> orderKits = orderKitRepository.findAll();
        return orderKits.stream()
                .map(orderTestKitMapper::toOrderTestKitResponse)
                .toList();
    }
}
