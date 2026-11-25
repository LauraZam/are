package com.university.edtech.service;

import com.university.edtech.dto.CourseRecommendationDto;
import com.university.edtech.model.Course;
import com.university.edtech.model.Enrollment;
import com.university.edtech.model.Student;
import com.university.edtech.repository.CourseRepository;
import com.university.edtech.repository.StudentRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final Counter recommendationRequestsCounter;
    private final Timer recommendationLatencyTimer;

    @Autowired
    public RecommendationService(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            MeterRegistry meterRegistry
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.recommendationRequestsCounter = Counter.builder("recommendations.requests.total")
                .description("Total number of recommendation requests")
                .register(meterRegistry);
        this.recommendationLatencyTimer = Timer.builder("recommendations.calculation.duration")
                .description("Duration of recommendation calculation")
                .register(meterRegistry);
    }

    @Transactional(readOnly = true)
    public List<CourseRecommendationDto> getRecommendations(Long studentId) {
        recommendationRequestsCounter.increment();
        return recommendationLatencyTimer.record(() -> computeRecommendations(studentId));
    }

    private List<CourseRecommendationDto> computeRecommendations(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        Set<Long> excludedCourseIds = student.getEnrollments().stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toSet());

        Set<Long> completedCourseIds = student.getEnrollments().stream()
                .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.COMPLETED)
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toSet());

        Set<String> studentSkills = student.getStudentSkills();
        List<Course> allCourses = courseRepository.findAll();

        List<CourseRecommendationDto> recommendations = new ArrayList<>();

        for (Course course : allCourses) {
            if (excludedCourseIds.contains(course.getId())) {
                continue;
            }

            if (course.getTrack() != null && !course.getTrack().equalsIgnoreCase(student.getMajorTrack())) {
                continue;
            }

            boolean prerequisitesMet = true;
            for (Course prereq : course.getPrerequisites()) {
                if (!completedCourseIds.contains(prereq.getId())) {
                    prerequisitesMet = false;
                    break;
                }
            }
            if (!prerequisitesMet) {
                continue;
            }

            Set<String> courseSkills = course.getCourseSkills();
            int matchingSkills = 0;
            for (String skill : courseSkills) {
                if (studentSkills.contains(skill)) {
                    matchingSkills++;
                }
            }

            double baseScore = courseSkills.isEmpty() ? 0.0 : (double) matchingSkills / courseSkills.size() * 100.0;

            if (course.getRequirementType() == Course.RequirementType.CORE) {
                baseScore += 25.0; // Core courses rank higher
            }

            recommendations.add(new CourseRecommendationDto(
                    course.getId(),
                    course.getCourseCode(),
                    course.getTitle(),
                    course.getCredits(),
                    matchingSkills,
                    baseScore
            ));
        }

        recommendations.sort((a, b) -> Double.compare(b.matchScore(), a.matchScore()));
        return recommendations;
    }
}