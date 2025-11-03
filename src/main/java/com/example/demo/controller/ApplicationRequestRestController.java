package com.example.demo.controller;

import com.example.demo.entity.ApplicationRequest;
import com.example.demo.entity.Course;
import com.example.demo.entity.Operator;
import com.example.demo.repository.ApplicationRequestRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ApplicationRequestRestController {

    @Autowired
    private ApplicationRequestRepository requestRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<ApplicationRequest> getAll() {
        return requestRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationRequest> getById(@PathVariable Long id) {
        return requestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApplicationRequest> create(@RequestBody ApplicationRequestDto requestDto) {
        ApplicationRequest request = new ApplicationRequest();
        request.setUserName(requestDto.getUserName());
        request.setCommentary(requestDto.getCommentary());
        request.setPhone(requestDto.getPhone());
        request.setHandled(requestDto.isHandled());
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        request.setCourse(course);
        ApplicationRequest saved = requestRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationRequest> update(@PathVariable Long id, @RequestBody ApplicationRequestDto requestDto) {
        return requestRepository.findById(id)
                .map(request -> {
                    request.setUserName(requestDto.getUserName());
                    request.setCommentary(requestDto.getCommentary());
                    request.setPhone(requestDto.getPhone());
                    request.setHandled(requestDto.isHandled());
                    if (requestDto.getCourseId() != null) {
                        Course course = courseRepository.findById(requestDto.getCourseId())
                                .orElseThrow(() -> new RuntimeException("Course not found"));
                        request.setCourse(course);
                    }
                    ApplicationRequest updated = requestRepository.save(request);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (requestRepository.existsById(id)) {
            requestRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DTO for request creation/update
    public static class ApplicationRequestDto {
        private String userName;
        private String commentary;
        private String phone;
        private boolean handled;
        private Long courseId;

        // Getters and setters
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCommentary() {
            return commentary;
        }

        public void setCommentary(String commentary) {
            this.commentary = commentary;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean isHandled() {
            return handled;
        }

        public void setHandled(boolean handled) {
            this.handled = handled;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }
    }
}