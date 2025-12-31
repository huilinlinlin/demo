package com.example.config;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Problem;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    List<Problem> findByProblemItem(String problemItem);
}