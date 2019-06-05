package com.app.quizzesapp.service;

import com.app.quizzesapp.exceptions.MyException;
import com.app.quizzesapp.model.Quiz;
import com.app.quizzesapp.model.User;
import com.app.quizzesapp.model.country.Country;
import com.app.quizzesapp.model.country.Region;
import com.app.quizzesapp.model.dto.QuizDto;
import com.app.quizzesapp.model.dto.UserDto;
import com.app.quizzesapp.model.dto.mappers.QuizMapper;
import com.app.quizzesapp.model.dto.mappers.UserMapper;
import com.app.quizzesapp.repository.CountryRepository;
import com.app.quizzesapp.repository.QuizRepository;
import com.app.quizzesapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class QuizService
{

    private final QuizRepository quizRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    public Optional<Quiz> generateQuiz(Long userId, Region region)
    {
        if (region == null)
        {
            throw new MyException("REGION VALUE IS NULL");
        }
        if (userId == null)
        {
            throw new MyException("USER ID IS NULL");
        }

        Quiz quiz = Quiz
                .builder()
                .user(userRepository.getOne(userId))
                .competition(false)
                .solved(false)
                .localDate(LocalDate.now())
                .countries(generateCountriesToQuiz(region))
                .answers(new LinkedList<>())
                .duration(Duration.ofSeconds(0))
                .score(0)
                .build();
        return Optional.of(quizRepository.save(quiz));
    }

    public void generateCompetitionQuiz(Long userId)
    {
        Long count = quizRepository.findAllByUserId(userId)
                .stream()
                .filter(quiz -> quiz.isCompetition() == true &&
                        quiz.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .count();

        Optional<Quiz> quiz;
        if (count != 0)
        {
            throw new MyException("USER ALREADY PARTICIPATED IN THE COMPETITION IN THIS MONTH");
        }
        else {
            quiz = generateQuiz(userId, Region.ALL);
            if (quiz.isPresent())
            {
                quiz.get().setCompetition(true);
            }
        }
    }

    public List<Country> solveQuiz(Long quizId)
    {
        if (quizId == null)
        {
            throw new MyException("QUIZ ID IS NULL");
        }
        Quiz quiz = quizRepository.getOne(quizId);
        if (quiz == null)
        {
            throw new MyException("QUIZ IS NULL");
        }

        quiz.setStart(LocalTime.now());

        List<Country> countries = quiz.getCountries();
        if (countries == null)
        {
            throw new MyException("COUNTRY LIST IS NULL");
        }
        return countries;
    }

    public Optional<QuizDto> checkAnswers(Long quizId, List<String> answers)
    {
        Quiz quiz = quizRepository.getOne(quizId);
        if (quiz.isSolved() == true)
        {
            throw new MyException("QUIZ IS ALREADY SOLVED");
        }
        if (quiz == null) {
            throw new MyException("QUIZ IS NULL");
        }
        if (answers.size() == 0) {
            throw new MyException("ANSWERS LIST IS EMPTY");
        }
        quiz.setEnd(LocalTime.now());
        quiz.setDuration(Duration.between(quiz.getStart(),quiz.getEnd()));
        quiz.setSolved(true);
        int count = 0;

        for (Country c : quiz.getCountries()) {
            if (c.getCapital().toUpperCase().equals(answers.get(count).toUpperCase()))
            {
                quiz.setScore(quiz.getScore()+1);
            }
            count++;
        }

        return Optional.of(quizMapper.quizToQuizDto(quiz));
    }

    //tested
    public Optional<UserDto> findCompetitionWinner()
    {
        User winner = quizRepository.findAll()
                .stream()
                .filter(quiz -> quiz.isCompetition() == true)
                .filter(quiz -> quiz.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .sorted(Comparator.comparing(Quiz::getScore).reversed().thenComparing(Quiz::getDuration))
                .findFirst()
                .map(quiz -> quiz.getUser())
                .get();

        return Optional.of(userMapper.userToUserDto(winner));
    }

    //tested
    public List<UserDto> scoreTable()
    {
        return quizRepository.findAll()
                .stream()
                .filter(quiz -> quiz.isCompetition() &&
                        quiz.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .sorted(Comparator.comparing(Quiz::getScore).reversed().thenComparing(Quiz::getDuration))
                .map(quiz -> userMapper.userToUserDto(quiz.getUser()))
                .collect(Collectors.toList());
    }

    //tested
    public List<QuizDto> sortAllByScore(String order)
    {
        if (order == null || !order.matches("(asc|desc)")) {
            throw new MyException("ILLEGAL ORDER NAME");
        }

        List<Quiz> sorted = quizRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Quiz::getScore))
                .collect(Collectors.toList());

        if (order.equals("desc")) {
            Collections.reverse(sorted);
        }

        return sorted.stream().map(quiz -> quizMapper.quizToQuizDto(quiz)).collect(Collectors.toList());
    }

    //tested
    public List<QuizDto> sortUserQuizzesByScore(String order, Long userId)
    {
        if (!order.matches("(asc|desc)") || order == null)
        {
            throw new MyException("ILLEGAL ORDER NAME");
        }
        else if (userId == null)
        {
            throw new MyException("USER ID IS NULL");
        }

        List<Quiz> sorted = quizRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Quiz::getScore))
                .collect(Collectors.toList());

        if (order.equals("desc"))
        {
            Collections.reverse(sorted);
        }

        return sorted.stream().map(quiz -> quizMapper.quizToQuizDto(quiz)).collect(Collectors.toList());

    }


    //tested
    public List<QuizDto> findAllQuizzesByUserId(Long userId)
    {
        if (userId == null)
        {
            throw new MyException("USER ID IS NULL");
        }
        return quizRepository.findAllByUserId(userId).stream().map(quiz -> quizMapper.quizToQuizDto(quiz)).collect(Collectors.toList());
    }


    //tested
    public Optional<QuizDto> getOneQuiz(Long quizId)
    {
        if (quizId == null)
        {
            throw new MyException("USER ID IS NULL");
        }
        return Optional.of(quizMapper.quizToQuizDto(quizRepository.getOne(quizId)));
    }


    //tested
    public List<QuizDto> findAllSolvedOrUnsolvedQuizzes(boolean isSolved, Long userId)
    {
        if (userId == null)
        {
            throw new MyException("USER ID IS NULL");
        }
        return quizRepository.findAll()
                .stream()
                .filter(quiz -> quiz.isSolved() == isSolved && quiz.getUser().getId().equals(userId))
                .map(quiz -> quizMapper.quizToQuizDto(quiz))
                .collect(Collectors.toList());
    }



    //tested
    public List<Country> generateCountriesToQuiz(Region region)
    {
        if (region == null)
        {
            throw new MyException("REGION IS NULL");
        }

        List<Long> numbers = getRandomNumbers(getCountryNumber(region));

        return countryRepository.findAll()
                .stream()
                .filter(country -> numbers.contains(country.getId()))
                .collect(Collectors.toList());
    }

    public List<Long> getRandomNumbers(List<Long> list)
    {

        Random rand = new Random();
        List numbers = new ArrayList();
        Long number;
        do {
            number = list.get(rand.nextInt(list.size()));
            if (!numbers.contains(number))
            {
                numbers.add(number);
            }
        }while (numbers.size() != 10L);
        return numbers;
    }

    public List<Long> getCountryNumber(Region region)
    {

        List<Country> countries = countryRepository.findAll();

        List<Long> europeCountriesAmount = countries
                .stream()
                .filter(country -> country.getRegion().equals("EUROPE"))
                .map(Country::getId)
                .collect(Collectors.toList());

        List<Long> americasCountriesAmount = countries
                .stream()
                .filter(country -> country.getRegion().equals("AMERICAS"))
                .map(Country::getId)
                .collect(Collectors.toList());

        List<Long> africaCountriesAmount = countries
                .stream()
                .filter(country -> country.getRegion().equals("AFRICA"))
                .map(Country::getId)
                .collect(Collectors.toList());

        List<Long> asiaCountriesAmount = countries
                .stream()
                .filter(country -> country.getRegion().equals("ASIA"))
                .map(Country::getId)
                .collect(Collectors.toList());


        List<Long> oceaniaCountriesAmount = countries
                .stream()
                .filter(country -> country.getRegion().equals("OCEANIA"))
                .map(Country::getId)
                .collect(Collectors.toList());


        if (region.equals(Region.EUROPE))
        {
            return europeCountriesAmount;
        }else if (region.equals(Region.ASIA))
        {
            return asiaCountriesAmount;
        }else if (region.equals(Region.AFRICA))
        {
            return africaCountriesAmount;
        }else if (region.equals(Region.AMERICAS))
        {
            return americasCountriesAmount;
        }
        else if (region.equals(Region.OCEANIA))
        {
            return oceaniaCountriesAmount;
        }else if (region.equals(Region.ALL))
        {
            return countries
                    .stream()
                    .map(Country::getId)
                    .collect(Collectors.toList());
        }
        else return new ArrayList<>();
    }
}

