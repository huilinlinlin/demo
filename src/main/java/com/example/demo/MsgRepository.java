package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MsgRepository extends JpaRepository<Msg, Integer> {
    List<Msg> findByMsgUserid(String msgUserid);
}