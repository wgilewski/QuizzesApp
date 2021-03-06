package com.app.quizzesapp.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "Users")
public class User
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;


    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<Quiz> quizzes;
}
