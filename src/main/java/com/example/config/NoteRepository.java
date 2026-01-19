package com.example.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByNoteItem(String noteItem);
    
    @Query("SELECT MAX(n.noteId) FROM Note n")
    Long findMaxId();
}