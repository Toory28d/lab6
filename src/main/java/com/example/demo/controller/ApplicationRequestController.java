package com.example.demo.controller;

import com.example.demo.entity.ApplicationRequest;
import com.example.demo.entity.Operator;
import com.example.demo.repository.ApplicationRequestRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/requests")
public class ApplicationRequestController {

    @Autowired
    private ApplicationRequestRepository requestRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("requests", requestRepository.findAll());
        return "requests/list";
    }

    @GetMapping("/unhandled")
    public String unhandled(Model model) {
        model.addAttribute("requests", requestRepository.findByHandled(false));
        return "requests/list";
    }

    @GetMapping("/processed")
    public String processed(Model model) {
        model.addAttribute("requests", requestRepository.findByHandled(true));
        return "requests/list";
    }

    @GetMapping("/new")
    public String newRequest(Model model) {
        model.addAttribute("request", new ApplicationRequest());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("operators", operatorRepository.findAll());
        return "requests/form";
    }

    @PostMapping
    public String save(@ModelAttribute ApplicationRequest request, @RequestParam Long courseId, @RequestParam(required = false) List<Long> operatorIds) {
        request.setCourse(courseRepository.findById(courseId).orElseThrow());
        if (request.getId() == null || !request.isHandled()) {
            if (operatorIds != null && !operatorIds.isEmpty()) {
                request.getOperators().clear();
                request.getOperators().addAll(operatorRepository.findAllById(operatorIds));
                request.setHandled(true);
            } else {
                request.setHandled(false);
            }
        }
        requestRepository.save(request);
        return "redirect:/requests";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("request", requestRepository.findById(id).orElseThrow());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("operators", operatorRepository.findAll());
        return "requests/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        requestRepository.deleteById(id);
        return "redirect:/requests";
    }

    @GetMapping("/{id}/process")
    public String processForm(@PathVariable Long id, Model model) {
        ApplicationRequest request = requestRepository.findById(id).orElseThrow();
        if (request.isHandled()) {
            return "redirect:/requests";
        }
        model.addAttribute("request", request);
        model.addAttribute("operators", operatorRepository.findAll());
        return "requests/process";
    }

    @PostMapping("/{id}/assign")
    public String assign(@PathVariable Long id, @RequestParam List<Long> operatorIds) {
        ApplicationRequest request = requestRepository.findById(id).orElseThrow();
        if (request.isHandled()) {
            return "redirect:/requests";
        }
        List<Operator> selectedOperators = operatorRepository.findAllById(operatorIds);
        request.getOperators().addAll(selectedOperators);
        if (!selectedOperators.isEmpty()) {
            request.setHandled(true);
        }
        requestRepository.save(request);
        return "redirect:/requests/" + id + "/operators";
    }

    @GetMapping("/{id}/operators")
    public String viewOperators(@PathVariable Long id, Model model) {
        ApplicationRequest request = requestRepository.findById(id).orElseThrow();
        model.addAttribute("request", request);
        model.addAttribute("operators", request.getOperators());
        return "requests/operators";
    }

    @GetMapping("/{requestId}/operators/{operatorId}/remove")
    public String removeOperator(@PathVariable Long requestId, @PathVariable Long operatorId) {
        ApplicationRequest request = requestRepository.findById(requestId).orElseThrow();
        Operator operator = operatorRepository.findById(operatorId).orElseThrow();
        request.getOperators().remove(operator);
        if (request.getOperators().isEmpty()) {
            request.setHandled(false);
        }
        requestRepository.save(request);
        return "redirect:/requests/" + requestId + "/operators";
    }
}