package com.example.demo.controller;

import com.example.demo.entity.ApplicationRequest;
import com.example.demo.entity.Operator;
import com.example.demo.repository.ApplicationRequestRepository;
import com.example.demo.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operators")
public class OperatorsRestController {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private ApplicationRequestRepository requestRepository;

    @GetMapping
    public List<Operator> getAll() {
        return operatorRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Operator> create(@RequestBody Operator operator) {
        Operator saved = operatorRepository.save(operator);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}/assign/{requestId}")
    public ResponseEntity<ApplicationRequest> assignToRequest(@PathVariable Long id, @PathVariable Long requestId) {
        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operator not found"));
        ApplicationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.getOperators().add(operator);
        if (!request.getOperators().isEmpty()) {
            request.setHandled(true);
        }
        ApplicationRequest updated = requestRepository.save(request);
        return ResponseEntity.ok(updated);
    }
}