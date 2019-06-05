package com.app.quizzesapp.model;

import com.app.quizzesapp.model.country.Country;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Quizzes")
public class Quiz
{
    @Id
    @GeneratedValue
    private Long id;

    private boolean solved;
    private boolean competition;
    private LocalDate localDate;
    private Integer score;
    private LocalTime start;
    private LocalTime end;

    private Duration duration;



    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "quiz_country",
            joinColumns = @JoinColumn(name = "quiz_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "country_id", referencedColumnName = "id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Country> countries;

    @ElementCollection
    @CollectionTable(
            name = "answers",
            joinColumns = @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<String> answers;

}


