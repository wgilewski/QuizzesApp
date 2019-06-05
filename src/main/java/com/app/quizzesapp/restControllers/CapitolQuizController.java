package com.app.quizzesapp.restControllers;

import com.app.quizzesapp.model.Quiz;
import com.app.quizzesapp.model.country.Country;
import com.app.quizzesapp.model.country.Region;
import com.app.quizzesapp.model.dto.QuizDto;
import com.app.quizzesapp.model.dto.UserDto;
import com.app.quizzesapp.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/capitolQuiz")
@RequiredArgsConstructor
public class CapitolQuizController {
    private final QuizService quizService;


    @GetMapping("/getOne/{quizId}")
    public ResponseEntity<QuizDto> getOneQuiz(@PathVariable Long quizId) {
        Optional<QuizDto> quizDto = quizService.getOneQuiz(quizId);


        System.err.println(quizDto.get());


        if (!quizDto.isPresent()) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok(quizDto.get());
    }

    @GetMapping("/allSorted/{order}")
    public ResponseEntity<List<QuizDto>> sortByScore(@PathVariable String order) {
        List<QuizDto> quizzes = quizService.sortAllByScore(order);
        if (quizzes == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quizzes);
    }

    @PostMapping("/generate/{region}/{userId}")
    public ResponseEntity<List<Country>> generateQuizByRegion(@PathVariable String region, @PathVariable Long userId) {
        Optional<Quiz> quiz = quizService.generateQuiz(userId, Region.valueOf(region.toUpperCase()));
        if (quiz.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quiz.get().getCountries());
    }

    @PostMapping("/generateCompetition/{userId}")
    public void generateCompetitionQuiz(@PathVariable Long userId) {
        quizService.generateCompetitionQuiz(userId);
    }

    @GetMapping("/solve/{quizId}")
    public ResponseEntity<List<Country>> solveQuiz(@PathVariable Long quizId) {
        List<Country> countries = quizService.solveQuiz(quizId);
        if (countries == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countries);
    }

    @PostMapping("/check/{quizId}")
    public ResponseEntity<QuizDto> checkAnswers(RequestEntity<List<String>> answers, @PathVariable Long quizId)
    {
        Optional<QuizDto> quizDto = quizService.checkAnswers(quizId, answers.getBody());
        if (!quizDto.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quizDto.get());
    }

    @GetMapping("/showCompetitionTable")
    public ResponseEntity<List<UserDto>> competitionTable() {
        List<UserDto> users = quizService.scoreTable();

        if (users == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }


}



