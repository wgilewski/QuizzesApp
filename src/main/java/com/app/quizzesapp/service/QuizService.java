package com.app.quizzesapp.service;

import com.app.quizzesapp.exception.MyException;
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


    public Optional<Quiz> generateQuiz(Long userId, String region)
    {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent())
        {
            throw new MyException("THERE IS NO USER WITH SUCH ID IN DB");
        }
        if (!List.of(Region.values()).contains(Region.valueOf(region)))
        {
            throw new MyException("REGION VALUE ERROR");
        }

        Quiz quiz = Quiz
                .builder()
                .user(userRepository.getOne(userId))
                .competition(false)
                .solved(false)
                .localDate(LocalDate.now())
                .countries(generateCountriesToQuiz(Region.valueOf(region)))
                .answers(new LinkedList<>())
                .duration(Duration.ofSeconds(0))
                .score(0)
                .build();

        if (quiz == null)
        {
            throw  new MyException("GENERATING QUIZ ERROR");
        }
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
            throw new MyException("USER HAS ALREADY PARTICIPATED IN THE COMPETITION IN THIS MONTH");
        }
        else {
            quiz = generateQuiz(userId, "ALL");
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
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        if (!quiz.isPresent())
        {
            throw new MyException("THERE IS NO QUIZ WITH SUCH ID IN DB");
        }
        if (quiz.get().isSolved() == true)
        {
            throw new MyException("QUIZ IS ALREADY SOLVED");
        }
        quiz.get().setStart(LocalTime.now());

        List<Country> countries = quiz.get().getCountries();
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
        if (quiz == null)
        {
            throw new MyException("CHECKING ANSWERS IN QUIZ ERROR");
        }
        return Optional.of(quizMapper.quizToQuizDto(quiz));

    }

    public Optional<UserDto> findCompetitionWinner()
    {
        Optional<UserDto> userDto =  Optional.of(quizRepository.findAll()
                .stream()
                .filter(quiz -> quiz.isCompetition() == true)
                .filter(quiz -> quiz.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .sorted(Comparator.comparing(Quiz::getScore).reversed().thenComparing(Quiz::getDuration))
                .findFirst()
                .map(quiz -> quiz.getUser())
                .map(user -> userMapper.userToUserDto(user))
                .orElseThrow(() -> new MyException("FINDING WINNER ERROR")));

        userDto.get().getQuizzes().forEach(quizDto -> quizDto.setCountries(List.of()));
        userDto.get().getQuizzes().forEach(quizDto -> quizDto.setAnswers(List.of()));

        return userDto;
    }

    public List<UserDto> scoreTable()
    {


        List<UserDto> users =  findAll().stream()
                .filter(quiz -> quiz.isCompetition() &&
                        quiz.getLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                .sorted(Comparator.comparing(QuizDto::getScore).reversed().thenComparing(QuizDto::getDuration))
                .map(quiz -> userMapper.userToUserDto(quiz.getUser()))
                .collect(Collectors.toList());

        users.forEach(userDto -> userDto.getQuizzes().forEach(quizDto -> quizDto.setCountries(List.of())));
        users.forEach(userDto -> userDto.getQuizzes().forEach(quizDto -> quizDto.setAnswers(List.of())));

        return users;

    }

    public List<QuizDto> sortAllByScoreWithoutCompetition(String order)
    {
        if (order == null || !order.matches("(asc|desc)")) {
            throw new MyException("ILLEGAL ORDER NAME");
        }

        List<QuizDto> sorted = findAll()
                .stream()
                .filter(quiz -> quiz.isCompetition() == false)
                .sorted(Comparator.comparing(QuizDto::getScore))
                .collect(Collectors.toList());

        if (order.equals("desc")) {
            Collections.reverse(sorted);
        }

        sorted.forEach(quiz -> quiz.setCountries(List.of()));
        sorted.forEach(quiz -> quiz.setAnswers(List.of()));

        return sorted;

    }

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

        List<QuizDto> sorted = findAll()
                .stream()
                .filter(quizDto -> quizDto.getUser().getId().equals(userId))
                .sorted(Comparator.comparing(QuizDto::getScore))
                .collect(Collectors.toList());

        if (order.equals("desc"))
        {
            Collections.reverse(sorted);
        }

        return sorted;

    }

    public List<QuizDto> findAllQuizzesByUserId(Long userId)
    {
        if (userId == null)
        {
            throw new MyException("USER ID IS NULL");
        }

        List<QuizDto> quizzes = findAll()
                .stream()
                .filter(quizDto -> quizDto.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        quizzes.forEach(quizDto -> quizDto.setAnswers(List.of()));
        quizzes.forEach(quizDto -> quizDto.setCountries(List.of()));

        return quizzes;
    }

    public Optional<QuizDto> getOneQuiz(Long quizId)
    {
        if (quizId == null)
        {
            throw new MyException("USER ID IS NULL");
        }
        Optional<QuizDto> quizDto =  quizRepository.findAll()
                .stream()
                .filter(quiz -> quiz.getId().equals(quizId))
                .map(quiz -> quizMapper.quizToQuizDto(quiz))
                .findFirst();

        if (quizDto.isPresent())
        {
            return quizDto;
        }throw new MyException("THERE IS NO QUIZ WITH SUCH ID IN DB");

    }

    public List<QuizDto> findAll()
    {
        List<QuizDto> quizzes = quizRepository.findAll()
                .stream()
                .map(quiz -> quizMapper.quizToQuizDto(quiz))
                .collect(Collectors.toList());

        if (quizzes == null)
        {
            throw new MyException("QUIZZES LIST IS NULL");
        }return quizzes;


    }

    public List<QuizDto> findAllSolvedOrUnsolvedQuizzes(boolean isSolved, Long userId)
    {
        if (userId == null)
        {
            throw new MyException("USER ID IS NULL");
        }



        List<QuizDto> quizzes = findAll()
                .stream()
                .filter(quizDto -> quizDto.getUser().getId().equals(userId))
                .filter(quizDto -> quizDto.isSolved() == isSolved)
                .collect(Collectors.toList());

        quizzes.forEach(quizDto -> quizDto.setCountries(List.of()));
        quizzes.forEach(quizDto -> quizDto.setAnswers(List.of()));

        return quizzes;
    }

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

