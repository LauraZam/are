package com.university.edtech.dto;

import jakarta.validation.constraints.NotBlank;

public record SkillRequestDto(
        @NotBlank(message = "Skill name cannot be blank")
        String skill
) {}