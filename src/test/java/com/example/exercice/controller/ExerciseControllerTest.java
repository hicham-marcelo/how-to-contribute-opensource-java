
package com.example.exercice.controller;


import com.example.exercice.model.dto.ExerciseDTO;
import com.example.exercice.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    private ExerciseDTO exercise;

    @BeforeEach
    void setUp() {
        exercise = ExerciseDTO.builder()
                .id(1L)
                .name("Test Exercise")
                .description("Test Exercise Description")
                .build();
    }

    @Test
    void getAllExercises() {
        when(exerciseService.findAll()).thenReturn(Collections.singletonList(exercise));

        var result = exerciseController.getAllExercises();

        assertEquals(Collections.singletonList(exercise), result);
    }

    @Test
    void getExerciseById() {
        when(exerciseService.findById(1L)).thenReturn(Optional.of(exercise));

        ResponseEntity<ExerciseDTO> response = exerciseController.getExerciseById(1L);

        assertEquals(ResponseEntity.ok(exercise), response);
    }

    @Test
    void createExercise() {
        when(exerciseService.save(exercise)).thenReturn(exercise);

        ResponseEntity<ExerciseDTO> response = exerciseController.createExercise(exercise);

        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(exercise), response);
    }

    @Test
    void updateExercise() {
        var updatedExercise = ExerciseDTO.builder()
                .id(1L)
                .name("Updated Name")
                .description("Updated Description")
                .build();

        when(exerciseService.findById(1L)).thenReturn(Optional.of(exercise));
        when(exerciseService.save(exercise)).thenReturn(updatedExercise);

        ResponseEntity<ExerciseDTO> response = exerciseController.updateExercise(1L, updatedExercise);

        assertEquals(ResponseEntity.ok(updatedExercise), response);
    }

    @Test
    void deleteExercise() {
        when(exerciseService.findById(1L)).thenReturn(Optional.of(exercise));

        ResponseEntity<Void> response = exerciseController.deleteExercise(1L);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(exerciseService).deleteById(1L);
    }
}