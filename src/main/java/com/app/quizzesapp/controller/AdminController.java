package com.app.quizzesapp.controller;


import com.app.quizzesapp.model.country.Country;
import com.app.quizzesapp.model.dto.UserDto;
import com.app.quizzesapp.service.CountryService;
import com.app.quizzesapp.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController
{
    private final CountryService countryService;
    private final QuizService quizService;

    @GetMapping("/downloadAndSaveAllCountries")
    public void generateCountries()
    {
        countryService.saveCountries();
    }

    @GetMapping("/allCountries")
    public ResponseEntity<List<Country>> getAll()
    {
        List<Country> countries = countryService.getAllCountriesFromApi();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/findWinner")
    public ResponseEntity<UserDto> findWinner()
    {
        Optional<UserDto> userDto = quizService.findCompetitionWinner();

        if (!userDto.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDto.get());
    }


}
