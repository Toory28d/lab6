package com.example.demo.repository;

import com.example.demo.entity.ApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRequestRepository extends JpaRepository<ApplicationRequest, Long> {
    List<ApplicationRequest> findByHandled(boolean handled);
}