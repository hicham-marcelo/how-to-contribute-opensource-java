package com.example.exercice.service;

import com.example.exercice.model.Exercise;
import com.example.exercice.model.dto.ExerciseDTO;
import com.example.exercice.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<ExerciseDTO> findAll() {
        return exerciseRepository.findAll().stream()
                .map(this::toExerciseDTO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList)
                );
    }

    public Optional<ExerciseDTO> findById(Long id) {
        var entity = exerciseRepository.findById(id);
        if (!entity.isPresent())
            throw new IllegalArgumentException("Exercise not found using id as parameter.");
        return Optional.ofNullable(toExerciseDTO(entity.get()));
    }

    public ExerciseDTO save(ExerciseDTO exercise) {
        validateExercise(exercise);
        var entity = toExerciseEntity(exercise);
        entity = exerciseRepository.save(entity);
        return toExerciseDTO(entity);
    }


    public void deleteById(Long id) {
        var entity = this.findById(id);
        if(!entity.isPresent())
            throw new IllegalArgumentException("Exercise not found using id as parameter.");
        exerciseRepository.deleteById(id);
    }

    private Exercise toExerciseEntity(ExerciseDTO dto){
        return Exercise.builder()
                .description(dto.getDescription())
                .name(dto.getName())
                .build();
    }

    private ExerciseDTO toExerciseDTO(Exercise entity){
        return ExerciseDTO.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .name(entity.getName())
                .build();
    }

    private void validateExercise(ExerciseDTO exercise){
        if(exercise.getDescription() == null || exercise.getDescription().isEmpty()){
            throw new IllegalArgumentException("Description cannot be empty.");
        }

        if(exercise.getName() == null || exercise.getName().isEmpty()){
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

}
