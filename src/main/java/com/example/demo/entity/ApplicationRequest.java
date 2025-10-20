package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ApplicationRequest {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String commentary;
    private String phone;
    private boolean handled;

    @ManyToOne
    private Course course;

    @ManyToMany
    @JoinTable(
            name = "request_operators",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "operator_id")
    )
    private List<Operator> operators = new ArrayList<>();
}