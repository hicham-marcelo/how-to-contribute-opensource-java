package com.example.exercice.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseDTO {
    private Long id;
    private String name;
    private String description;
}
