package com.app.quizzesapp.service;

import com.app.quizzesapp.exception.MyException;
import com.app.quizzesapp.model.country.Country;
import com.app.quizzesapp.model.country.informations.Details;
import com.app.quizzesapp.model.dto.CountryDto;
import com.app.quizzesapp.model.dto.QuizDto;
import com.app.quizzesapp.model.localization.Localization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DetailsService
{

    private final LocalizationService localizationService;
    private final CountryService countryService;


    public Optional<Details> theMostDistantCity(QuizDto quizDto)
    {
        if (quizDto == null)
        {
            throw new MyException("QUIZDTO IS NULL");
        }

        Optional<Localization> myLocalization = localizationService.findMyLocalization();
        if (!myLocalization.isPresent())
        {
            throw new MyException("LOCALIZATION IS NULL");
        }

        if (quizDto.getCountries() == null || quizDto.getCountries().size() == 0)
        {
            throw new MyException("COUNTRY LIST FROM QUIZ IS NULL");
        }

        String myLocalizationTown = myLocalization.get().city;

        Map<String,Double> capitalDistance = new HashMap<>();
        List<CountryDto> capitalsFromQuiz = quizDto.getCountries();

        for (CountryDto c : capitalsFromQuiz)
        {
            capitalDistance.put(c.getCapital(),getDistance(c.getCapital(),myLocalizationTown)
                    .orElseThrow(() -> new MyException("DISTANCE VALUE IS NULL")));
        }

        Optional<String> result = capitalDistance.entrySet()
                .stream()
                .filter(stringDoubleEntry -> stringDoubleEntry.getValue() > 0)
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .findFirst()
                .map(Map.Entry::getKey);

        return countryService.getDistanceAndCuriosities(result.get(),myLocalizationTown);
    }

    public Optional<Double> getDistance(String capital1, String capital2)
    {
        Optional<Details> details = countryService.getDistanceAndCuriosities(capital1,capital2);
        if (!details.isPresent())
        {
            throw new MyException("DETAILS ARE NULL");
        }
        return Optional.of(details.get().getDistance());
    }
}
