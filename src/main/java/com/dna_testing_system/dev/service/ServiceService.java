package com.dna_testing_system.dev.service;

import com.dna_testing_system.dev.entity.DnaService;
import com.dna_testing_system.dev.repository.DnaServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private DnaServiceRepository dnaServiceRepository;

    public List<DnaService> getAllServices() {
        return dnaServiceRepository.findAll();
    }

    public Optional<DnaService> getServiceById(Long id) {
        return dnaServiceRepository.findById(id);
    }

    public void saveService(DnaService service) {
        dnaServiceRepository.save(service);
    }

    public void deleteService(Long id) {
        dnaServiceRepository.deleteById(id);
    }
}