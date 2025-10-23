package com.example.config;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByNoteItem(String noteItem);
}