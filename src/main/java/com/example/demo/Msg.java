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
@Table(name = "MSG")
public class Msg {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="MSG_ID")
  private Integer msgId; 
   @Column(name ="MSG_USER_ID")
  private String msgUserid;
   @Column(name ="MSG_CONTENT")
  private String msgContent;
   @Column(name ="MSG_DATE")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime msgDate;
  
  public Integer getMsgId() {
    return msgId;
  }
  public void setMsgId(Integer msgId) {
    this.msgId = msgId;
  }
  public String getMsgUserid() {
    return msgUserid;
  }
  public void setMsgUserid(String msgUserid) {
    this.msgUserid = msgUserid;
  }
  public String getMsgContent() {
    return msgContent;
  }
  public void setMsgContent(String msgContent) {
    this.msgContent = msgContent;
  }
  public LocalDateTime getMsgDate() {
    return msgDate;
  }
  public void setMsgDate(LocalDateTime msgDate) {
    this.msgDate = msgDate;
    System.out.println(msgDate);
  }
  @Override
  public String toString(){
    return "MSG = msgId:"+msgId
            +", msgUserid:"+msgUserid
            +", msgContent:"+msgContent
            +", msgDate:"+msgDate;
  }
  
}