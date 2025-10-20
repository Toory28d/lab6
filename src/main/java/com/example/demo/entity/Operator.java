package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Operator {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String department;

    @ManyToMany(mappedBy = "operators")
    private List<ApplicationRequest> requests = new ArrayList<>();
}