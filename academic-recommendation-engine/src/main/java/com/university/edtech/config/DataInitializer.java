package com.university.edtech.config;

import com.university.edtech.model.Course;
import com.university.edtech.model.Enrollment;
import com.university.edtech.model.Student;
import com.university.edtech.repository.CourseRepository;
import com.university.edtech.repository.EnrollmentRepository;
import com.university.edtech.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (courseRepository.existsByCourseCode("CS-404")) {
                return;
            }

            Course java101 = Course.builder()
                    .courseCode("CS-101")
                    .title("Introduction to Java Programming")
                    .description("Fundamentals of object-oriented programming in Java.")
                    .credits(3)
                    .track("COMPUTER_SCIENCE")
                    .requirementType(Course.RequirementType.CORE)
                    .courseSkills(Set.of("Java", "OOP"))
                    .build();

            Course springBoot = Course.builder()
                    .courseCode("CS-404")
                    .title("Advanced Backend Development")
                    .description("Building enterprise REST APIs with Spring Boot and PostgreSQL.")
                    .credits(4)
                    .track("COMPUTER_SCIENCE")
                    .requirementType(Course.RequirementType.CORE)
                    .courseSkills(Set.of("Java", "Spring Boot", "SQL", "REST API"))
                    .build();

            Course finance101 = Course.builder()
                    .courseCode("FIN-3210")
                    .title("Corporate Finance")
                    .description("Financial statement analysis, working capital, and valuation.")
                    .credits(3)
                    .track("FINANCE")
                    .requirementType(Course.RequirementType.ELECTIVE)
                    .courseSkills(Set.of("Excel", "Financial Analysis", "Ratio Analysis"))
                    .build();

            springBoot.getPrerequisites().add(java101);

            courseRepository.saveAll(Set.of(java101, springBoot, finance101));

            Student studentJane = Student.builder()
                    .studentNumber("S-2026-001")
                    .firstName("Jane")
                    .lastName("Smith")
                    .email("jane.smith@university.edu")
                    .academicYear(3)
                    .majorTrack("COMPUTER_SCIENCE")
                    .studentSkills(Set.of("Java", "OOP", "SQL"))
                    .build();

            studentRepository.save(studentJane);

            Enrollment completedJava = Enrollment.builder()
                    .student(studentJane)
                    .course(java101)
                    .semester("Fall 2025")
                    .status(Enrollment.EnrollmentStatus.COMPLETED)
                    .grade("A")
                    .build();

            enrollmentRepository.save(completedJava);

            System.out.println("--> Sample database seed with tracks completed successfully!");
        };
    }
}