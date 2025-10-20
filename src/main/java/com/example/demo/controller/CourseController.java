package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "courses/list";
    }

    @GetMapping("/new")
    public String newCourse(Model model) {
        model.addAttribute("course", new Course());
        return "courses/form";
    }

    @PostMapping
    public String save(@ModelAttribute Course course) {
        courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseRepository.findById(id).orElseThrow());
        return "courses/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return "redirect:/courses";
    }
}