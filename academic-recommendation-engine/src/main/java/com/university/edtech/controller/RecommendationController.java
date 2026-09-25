package com.university.edtech.controller;

import com.university.edtech.dto.CourseRecommendationDto;
import com.university.edtech.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CourseRecommendationDto>> getRecommendationsForStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(studentId));
    }
}