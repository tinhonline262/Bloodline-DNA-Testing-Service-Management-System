package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.Customer;
import com.dna_testing_system.dev.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer save(Customer customer) {
        logger.info("Đang lưu khách hàng: {}", customer.getName());

        try {
            if (customerRepository.existsByPhone(customer.getPhone())) {
                logger.warn("Số điện thoại đã tồn tại");
                throw new IllegalStateException("Số điện thoại đã được đăng ký!");
            }

            if (customerRepository.existsByEmail(customer.getEmail())) {
                logger.warn("Email đã tồn tại");
                throw new IllegalStateException("Email đã được đăng ký!");
            }

            Customer savedCustomer = customerRepository.save(customer);
            logger.info("Lưu khách hàng thành công với ID: {}", savedCustomer.getId());
            return savedCustomer;

        } catch (DataIntegrityViolationException e) {
            logger.error("Lỗi ràng buộc dữ liệu khi lưu khách hàng: {}", e.getMessage());
            throw new IllegalStateException("Email hoặc số điện thoại đã được đăng ký!");
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi lưu khách hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Có lỗi xảy ra khi lưu thông tin khách hàng!");
        }
    }

    public Optional<Customer> findByPhone(String phone) {
        logger.debug("Tìm khách hàng theo số điện thoại");
        try {
            return customerRepository.findByPhone(phone);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm khách hàng theo số điện thoại: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<Customer> findByEmail(String email) {
        logger.debug("Tìm khách hàng theo email");
        try {
            return customerRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm khách hàng theo email: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<Customer> findById(Long id) {
        logger.debug("Tìm khách hàng theo ID: {}", id);
        try {
            return customerRepository.findById(id);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm khách hàng theo ID: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public boolean existsByPhone(String phone) {
        logger.debug("Kiểm tra tồn tại số điện thoại");
        try {
            return customerRepository.existsByPhone(phone);
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra số điện thoại: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean existsByEmail(String email) {
        logger.debug("Kiểm tra tồn tại email");
        try {
            return customerRepository.existsByEmail(email);
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra email: {}", e.getMessage(), e);
            return false;
        }
    }

    @Transactional
    public Customer update(Customer customer) {
        logger.info("Đang cập nhật khách hàng ID: {}", customer.getId());
        try {
            if (!customerRepository.existsById(customer.getId())) {
                logger.warn("Không tìm thấy khách hàng với ID: {}", customer.getId());
                throw new IllegalArgumentException("Khách hàng không tồn tại!");
            }

            Customer updatedCustomer = customerRepository.save(customer);
            logger.info("Cập nhật khách hàng thành công với ID: {}", updatedCustomer.getId());
            return updatedCustomer;

        } catch (DataIntegrityViolationException e) {
            logger.error("Lỗi ràng buộc dữ liệu khi cập nhật khách hàng: {}", e.getMessage());
            throw new IllegalStateException("Email hoặc số điện thoại đã được sử dụng bởi khách hàng khác!");
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi cập nhật khách hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật thông tin khách hàng!");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Đang xóa khách hàng với ID: {}", id);
        try {
            if (!customerRepository.existsById(id)) {
                logger.warn("Không tìm thấy khách hàng với ID: {}", id);
                throw new IllegalArgumentException("Khách hàng không tồn tại!");
            }

            customerRepository.deleteById(id);
            logger.info("Xóa khách hàng thành công với ID: {}", id);

        } catch (Exception e) {
            logger.error("Lỗi khi xóa khách hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Có lỗi xảy ra khi xóa khách hàng!");
        }
    }
}