package com.university.edtech.controller;

import com.university.edtech.dto.SkillRequestDto;
import com.university.edtech.dto.StudentRequestDto;
import com.university.edtech.dto.StudentResponseDto;
import com.university.edtech.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Set;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody StudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id, @RequestBody StudentRequestDto dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<Set<String>> getStudentSkills(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentSkills(id));
    }

    @PostMapping("/{id}/skills")
    public ResponseEntity<Set<String>> addSkill(@PathVariable Long id, @RequestBody @Valid SkillRequestDto request) {
        return ResponseEntity.ok(studentService.addSkillToStudent(id, request.skill()));
    }

    @DeleteMapping("/{id}/skills")
    public ResponseEntity<Set<String>> removeSkill(@PathVariable Long id, @RequestParam String skill) {
        return ResponseEntity.ok(studentService.removeSkillFromStudent(id, skill));
    }
}