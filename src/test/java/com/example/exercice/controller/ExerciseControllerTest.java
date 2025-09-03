
package com.example.exercice.controller;

import com.example.exercice.model.Exercise;
import com.example.exercice.repository.ExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllExercises_shouldReturnListOfExercises() throws Exception {
        Exercise exercise1 = new Exercise("Exercise 1", "Description 1");
        Exercise exercise2 = new Exercise("Exercise 2", "Description 2");
        when(exerciseRepository.findAll()).thenReturn(Arrays.asList(exercise1, exercise2));

        mockMvc.perform(get("/exercise"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Exercise 1"))
                .andExpect(jsonPath("$[1].name").value("Exercise 2"));
    }

    @Test
    public void getExerciseById_shouldReturnExercise() throws Exception {
        Exercise exercise = new Exercise("Exercise 1", "Description 1");
        exercise.setId(1L);
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        mockMvc.perform(get("/exercise/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Exercise 1"));
    }

    @Test
    public void createExercise_shouldReturnCreatedExercise() throws Exception {
        Exercise exercise = new Exercise("New Exercise", "New Description");
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        mockMvc.perform(post("/exercise")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exercise)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Exercise"));
    }

    @Test
    public void updateExercise_shouldReturnUpdatedExercise() throws Exception {
        Exercise existingExercise = new Exercise("Existing Exercise", "Existing Description");
        existingExercise.setId(1L);
        Exercise updatedExercise = new Exercise("Updated Exercise", "Updated Description");
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(updatedExercise);

        mockMvc.perform(put("/exercise/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedExercise)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Exercise"));
    }

    @Test
    public void deleteExercise_shouldReturnNoContent() throws Exception {
        Exercise exercise = new Exercise("Exercise to delete", "Description");
        exercise.setId(1L);
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        mockMvc.perform(delete("/exercise/1"))
                .andExpect(status().isNoContent());
    }
}
