
package com.example.exercice.controller;

import com.example.exercice.model.dto.ExerciseDTO;
import com.example.exercice.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {


    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("")
    public List<ExerciseDTO> getAllExercises() {
        return exerciseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) {
        Optional<ExerciseDTO> exercise = exerciseService.findById(id);
        return exercise.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseDTO exercise) {
        ExerciseDTO savedExercise = exerciseService.save(exercise);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(@PathVariable Long id, @RequestBody ExerciseDTO exerciseDetails) {
        Optional<ExerciseDTO> optionalExercise = exerciseService.findById(id);

        if (optionalExercise.isPresent()) {
            ExerciseDTO existingExercise = optionalExercise.get();
            existingExercise.setName(exerciseDetails.getName());
            existingExercise.setDescription(exerciseDetails.getDescription());
            ExerciseDTO updatedExercise = exerciseService.save(existingExercise);
            return ResponseEntity.ok(updatedExercise);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        Optional<ExerciseDTO> optionalExercise = exerciseService.findById(id);

        if (optionalExercise.isPresent()) {
            exerciseService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
