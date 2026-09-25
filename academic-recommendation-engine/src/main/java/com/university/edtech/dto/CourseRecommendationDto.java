package com.university.edtech.dto;

public record CourseRecommendationDto(
        Long id,
        String courseCode,
        String title,
        Integer credits,
        int matchingSkillCount,
        double matchScore
) {}