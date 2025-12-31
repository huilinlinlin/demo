package com.example.entity;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "PROBLEM")
public class Problem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="PROBLEM_ID")
  private Integer problemId; 
   @Column(name ="PROBLEM_ITEM")
  private String problemItem;
   @Column(name ="PROBLEM_CONTENT")
  private String problemContent;
  @Column(name ="PROBLEM_FILE")
  private String problemFile;
   @Column(name ="PROBLEM_DATE")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime problemDate;
  
  public Integer getProblemId() {
    return problemId;
  }
  public void setProblemId(Integer problemId) {
    this.problemId = problemId;
  }
  public String getProblemItem() {
    return problemItem;
  }
  public void setProblemItem(String problemItem) {
    this.problemItem = problemItem;
  }
  public String getProblemContent() {
    return problemContent;
  }
  public void setProblemContent(String problemContent) {
    this.problemContent = problemContent;
  }
  public String getProblemFile() {
    return problemFile;
  }
  public void setProblemFile(String problemFile) {
    this.problemFile = problemFile;
  }
  public LocalDateTime getProblemDate() {
    return problemDate;
  }
  public void setProblemDate(LocalDateTime problemDate) {
    this.problemDate = problemDate;
  }
  @Override
  public String toString(){
    return "PROBLEM = problemId:"+problemId
            +", problemItem:"+problemItem
            +", problemContent:"+problemContent
            +", problemFile:"+problemFile
            +", problemDate:"+problemDate;
  }
  
}