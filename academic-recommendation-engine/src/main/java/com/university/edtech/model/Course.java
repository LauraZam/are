package com.university.edtech.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.time.OffsetDateTime;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @ManyToMany
    @JoinTable(
            name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    @Builder.Default
    private Set<Course> prerequisites = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "course_skills", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "skill")
    @Builder.Default
    private Set<String> courseSkills = new HashSet<>();

    @Column(nullable = false)
    private String track;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequirementType requirementType = RequirementType.ELECTIVE;

    public enum RequirementType {
        CORE, ELECTIVE
    }
}