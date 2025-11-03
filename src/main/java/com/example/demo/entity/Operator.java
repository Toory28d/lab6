package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<ApplicationRequest> requests = new ArrayList<>();
}