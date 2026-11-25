package com.university.edtech.service;

import com.university.edtech.model.Course;
import com.university.edtech.model.Student;
import com.university.edtech.repository.CourseRepository;
import com.university.edtech.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testStudent = Student.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .majorTrack("COMPUTER_SCIENCE")
                .studentSkills(new HashSet<>(Arrays.asList("Java", "Spring Boot")))
                .build();

        testCourse = Course.builder()
                .id(101L)
                .courseCode("CS4040")
                .title("Advanced Backend Development")
                .credits(3)
                .track("COMPUTER_SCIENCE")
                .courseSkills(new HashSet<>(Arrays.asList("Java", "Docker")))
                .build();
    }

    @Test
    void testRecommendationMatchingScoreCalculation() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));

        var recommendations = recommendationService.getRecommendationsForStudent(1L);

        assertNotNull(recommendations, "Recommendations list should not be null");
        assertFalse(recommendations.isEmpty(), "Recommendations list should contain matching courses");
    }
}