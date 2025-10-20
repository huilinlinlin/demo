package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")  // 允許前端的位址
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    // 查全部
    @GetMapping("/index")
    public List<Note> getAllNotes() {
        return noteRepository.findAll(Sort.by(Sort.Direction.DESC,"noteDate"));
    }

    // 依 ID 查
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Integer id) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        return optionalNote.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 查某個使用者的留言
    @GetMapping("/item/{item}")
    public List<Note> getNotesByUserId(@PathVariable String item) {
        return noteRepository.findByNoteItem(item);
    }

    // 新增留言
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Note createNote(
        @RequestParam("noteItem") String noteItem,
        @RequestParam("noteContent") String noteContent,
        @RequestParam(value = "noteFile", required = false) MultipartFile file,
        @RequestParam("noteDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime noteDate
    ){ 
        // 建立 Note 實體
        Note note = new Note();
        note.setNoteItem(noteItem);
        note.setNoteContent(noteContent);
        note.setNoteDate(noteDate);
        noteRepository.save(note);
        Optional.ofNullable(file).ifPresent(f -> saveFile(note.getNoteId(), f));
        return note;
    }

    // 更新留言
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
        @PathVariable Integer id,
        @RequestParam("noteItem") String noteItem,
        @RequestParam("noteContent") String noteContent,
        @RequestParam(value = "noteFile", required = false) MultipartFile file,
        @RequestParam("noteDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime noteDate) {
            // 儲存檔案到本地目錄            
        return noteRepository.findById(id)
                .map(note -> {
                    note.setNoteItem(noteItem);
                    note.setNoteContent(noteContent);
                    note.setNoteDate(noteDate);
                    Note savedNote = noteRepository.save(note);
                    Optional.ofNullable(file).ifPresent(f -> saveFile(note.getNoteId(), f));
                    return ResponseEntity.ok(savedNote);
                })
                .orElse(ResponseEntity.notFound().build()); 
    }

    // 刪除留言
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Integer id) {    
        if (noteRepository.existsById(id)) {
            System.out.println("刪除");//noteRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("刪除成功");
        }
        return ResponseEntity.status(HttpStatus.OK).body("刪除失敗");
    }

    //上傳檔案
    public void saveFile ( Integer noteId, MultipartFile file){
        String uploadDir = "C:\\DataSource\\notefile\\";
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename == null ? "txt" : originalFilename.substring(originalFilename.lastIndexOf('.')+1);
        String fileName = noteId.toString()+"."+extension;
        Path filePath = Paths.get(uploadDir , fileName);
        
        System.out.println("副檔名：" + fileName);
        try {
            Files.createDirectories(filePath.getParent()); // 確保目錄存在
            Files.write(filePath, file.getBytes());
            noteRepository.findById(noteId).ifPresent(note ->{
                note.setNoteFile(fileName);
                noteRepository.save(note);
            });
           
        } catch (IOException e) {
           System.out.println(e);
        } 
    }

    //下載檔案
    @GetMapping("/download.do")
    public void download(@RequestParam String id,HttpServletResponse response) {
        String fileName = id + ".txt";
        String filePath = "C:\\DataSource\\notefile";
        File file = new File (filePath+File.separator+fileName);
        //File folder = new File("C:\\DataSource\\notefile");
        if (!file.exists() || ! new File(filePath).isDirectory()){
            System.out.println("檔案或目錄不存在");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }
        try (FileInputStream fis = new FileInputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())){
            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.setContentLength((int) file.length()); 
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch (Exception e) {
           System.out.println("下載檔案失敗: "+e.toString());
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    //讀取檔案
    @GetMapping("/readFile/{id}")
    public String readFile(@PathVariable Integer id) {
    

        return "123";
    }
}
