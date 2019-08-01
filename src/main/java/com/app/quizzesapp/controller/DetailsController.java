package com.app.quizzesapp.controller;

import com.app.quizzesapp.model.country.informations.Details;
import com.app.quizzesapp.model.dto.QuizDto;
import com.app.quizzesapp.service.CountryService;
import com.app.quizzesapp.service.DetailsService;
import com.app.quizzesapp.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/details")
@RequiredArgsConstructor
public class DetailsController
{
    private final QuizService quizService;
    private final CountryService countryService;
    private final DetailsService detailsService;

    @GetMapping("/theMostDistantCapital/{quizId}")
    public ResponseEntity<Details> theMostDistantCapital(@PathVariable Long quizId)
    {
        Optional<QuizDto> quiz = quizService.getOneQuiz(quizId);
        Optional<Details> details = null;
        if (quiz.isPresent())
        {
           details = detailsService.theMostDistantCity(quiz.get());
        }
        if (details.isPresent())
        {
            return ResponseEntity.ok(details.get());
        }return ResponseEntity.notFound().build();

    }

    @GetMapping("/distanceBetweenTwoCountries/{town1}/{town2}")
    public ResponseEntity<Details> distance(@PathVariable String town1, @PathVariable String town2)
    {
        Optional<Details> informations = countryService.getDistanceAndCuriosities(town1, town2);
        if (informations.isPresent()) {
            return ResponseEntity.ok(informations.get());
        }
        return ResponseEntity.notFound().build();
    }

}
