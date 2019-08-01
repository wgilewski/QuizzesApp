package com.app.quizzesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuizzesappApplication {

    public static void main(String[] args)
    {
        //CONUTRYN SERVICE
        //list<String> list = null;
        // czy po zajsciu ifa new arraylist()
        // sprawdzać if(list == null) czy if (list.length == 0)

        //countryDetails ścieżka do api czy ma być final

        //conutryDetails - czy dobra praktyką jest ustawiać na początku klasę na null;

        // czy ja dobrze używam optionala.
        // getAllCountries, czy da się jakoś opakwoać w klasę requesta.



        //DETAILS SERVICE

        //OSTATNIA METEODA -jeśli zwracam optional<> to jak pobrać wartość z tej metody. wystarczy samo get() ?





        //QUIZ SERVICE

        //generateCompetitionQuiz ta metoda aktualizuje wartość isCompetition
        // , czy jeżeli zapisałem już quiz używając metody generateQuiz, to czy ta metoda może być void.




        SpringApplication.run(QuizzesappApplication.class, args);
    }

}
