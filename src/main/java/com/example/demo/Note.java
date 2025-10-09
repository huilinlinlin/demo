package com.example.demo;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "NOTE")
public class Note {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="NOTE_ID")
  private Integer noteId; 
   @Column(name ="ITEM")
  private String noteItem;
   @Column(name ="NOTE_CONTENT")
  private String noteContent;
  @Column(name ="NOTE_FILE")
  private String noteFile;
   @Column(name ="NOTE_DATE")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime noteDate;
  
  public Integer getNoteId() {
    return noteId;
  }
  public void setNoteId(Integer noteId) {
    this.noteId = noteId;
  }
  public String getNoteItem() {
    return noteItem;
  }
  public void setNoteItem(String noteItem) {
    this.noteItem = noteItem;
  }
  public String getNoteContent() {
    return noteContent;
  }
  public void setNoteContent(String noteContent) {
    this.noteContent = noteContent;
  }
  public String getNoteFile() {
    return noteFile;
  }
  public void setNoteFile(String noteFile) {
    this.noteFile = noteFile;
  }
  public LocalDateTime getNoteDate() {
    return noteDate;
  }
  public void setNoteDate(LocalDateTime noteDate) {
    this.noteDate = noteDate;
  }
  @Override
  public String toString(){
    return "NOTE = noteId:"+noteId
            +", noteItem:"+noteItem
            +", noteContent:"+noteContent
            +", noteFile:"+noteFile
            +", noteDate:"+noteDate;
  }
  
}