package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")  // 允許前端的位址
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    private MsgRepository msgRepository;

    // 查全部
    @GetMapping("/index")
    public List<Msg> getAllMsgs() {
        return msgRepository.findAll();
    }

    // 依 ID 查
    @GetMapping("/{id}")
    public ResponseEntity<Msg> getMsgById(@PathVariable Integer id) {
        Optional<Msg> optionalMsg = msgRepository.findById(id);
        return optionalMsg.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 查某個使用者的留言
    @GetMapping("/user/{userId}")
    public List<Msg> getMsgsByUserId(@PathVariable String userId) {
        return msgRepository.findByMsgUserid(userId);
    }

    // 新增留言
    @PostMapping
    public Msg createMsg(@RequestBody Msg msg) {
        System.out.println(msg.toString());
        return msgRepository.save(msg);
    }

    // 更新留言
    @PutMapping("/{id}")
    public ResponseEntity<Msg> updateMsg(@PathVariable Integer id, @RequestBody Msg updatedMsg) {
        return msgRepository.findById(id)
                .map(msg -> {
                    msg.setMsgUserid(updatedMsg.getMsgUserid());
                    msg.setMsgContent(updatedMsg.getMsgContent());
                    msg.setMsgDate(updatedMsg.getMsgDate());
                    Msg savedMsg = msgRepository.save(msg);
                    return ResponseEntity.ok(savedMsg);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 刪除留言
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMsg(@PathVariable Integer id) {
        if (msgRepository.existsById(id)) {
            msgRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
