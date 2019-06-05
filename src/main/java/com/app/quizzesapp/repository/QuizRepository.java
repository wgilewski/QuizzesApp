package com.app.quizzesapp.repository;

import com.app.quizzesapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long>
{
    List<Quiz> findAllByUserId(Long id);

}
