// src/main/java/com/example/demo/controller/OperatorController.java
package com.example.demo.controller;

import com.example.demo.entity.Operator;
import com.example.demo.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operators")
public class OperatorController {

    @Autowired
    private OperatorRepository operatorRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("operators", operatorRepository.findAll());
        return "operators/list";
    }

    @GetMapping("/new")
    public String newOperator(Model model) {
        model.addAttribute("operator", new Operator());
        return "operators/form";
    }

    @PostMapping
    public String save(@ModelAttribute Operator operator) {
        operatorRepository.save(operator);
        return "redirect:/operators";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("operator", operatorRepository.findById(id).orElseThrow());
        return "operators/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        operatorRepository.deleteById(id);
        return "redirect:/operators";
    }
}