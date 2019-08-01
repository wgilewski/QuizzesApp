package com.app.quizzesapp.controller;

import com.app.quizzesapp.model.User;
import com.app.quizzesapp.model.dto.QuizDto;
import com.app.quizzesapp.model.dto.UserDto;
import com.app.quizzesapp.model.localization.Localization;
import com.app.quizzesapp.service.LocalizationService;
import com.app.quizzesapp.service.QuizService;
import com.app.quizzesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final LocalizationService localizationService;
    private final QuizService quizService;


    @PostMapping
    public ResponseEntity<User> registerNewUser(RequestEntity<UserDto> request)
    {
        return ResponseEntity.ok(userService.registerUser(request.getBody()));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<QuizDto>> showAllQuizzes(@PathVariable Long userId)
    {
        List<QuizDto> quizzes = quizService.findAllQuizzesByUserId(userId);

        if (quizzes == null)
        {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/solved/{userId}")
    public ResponseEntity<List<QuizDto>> getSolved(@PathVariable Long userId)
    {
        List<QuizDto> quizzes = quizService.findAllSolvedOrUnsolvedQuizzes(true,userId);

        if (quizzes == null)
        {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/unsolved/{userId}")
    public ResponseEntity<List<QuizDto>> getUnsolved(@PathVariable Long userId)
    {
        List<QuizDto> quizzes = quizService.findAllSolvedOrUnsolvedQuizzes(false,userId);

        if (quizzes == null)
        {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/allSorted/{userId}/{order}")
    public ResponseEntity<List<QuizDto>> sortByScore(@PathVariable Long userId, @PathVariable String order)
    {
        List<QuizDto> quizzes = quizService.sortUserQuizzesByScore(order,userId);

        if (quizzes == null)
        {
            return ResponseEntity.notFound().build();
        }else return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/myLocalization")
    public ResponseEntity<Localization> getMyLocalization() {
        Optional<Localization> myLocalization = localizationService.findMyLocalization();

        if (myLocalization.isPresent())
        {
            return ResponseEntity.ok(myLocalization.get());
        }
        return ResponseEntity.notFound().build();
    }


}
