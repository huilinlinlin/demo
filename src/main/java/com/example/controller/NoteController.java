package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Note;
import java.util.Base64;

import com.example.config.NoteRepository;

import jakarta.servlet.http.HttpServletRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")  // 允許前端的位址
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;
    private String noteFilePath = "C:\\DataSource\\notefile";

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
        @RequestParam(value = "noteImgFile", required = false) MultipartFile noteImgFile,
        @RequestParam(value = "noteTextFile", required = false) MultipartFile noteTextFile,
        @RequestParam("noteDate") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime noteDate
    )throws IOException{ 
        // 建立 Note 實體
        Note note = new Note();
        note.setNoteItem(noteItem);
        note.setNoteContent(noteContent);
        note.setNoteDate(noteDate);
        String noteFile = uploadfile (noteImgFile) + uploadfile (noteTextFile);
        note.setNoteFile(noteFile.length() > 1 ? noteFile.substring(0, noteFile.length() - 1) : "");
        noteRepository.save(note);
        return note;
    }
    public String uploadfile (MultipartFile noteFile) throws IOException{
        Long nextNoteId = noteRepository.findMaxId() ;
        String newNoteId = String.valueOf(( nextNoteId == null ? 0L : nextNoteId ) +1 );
        String newFileName = ""; 
        if(noteFile != null && !noteFile.isEmpty()){
            Path dirPath = Paths.get(noteFilePath);
            String originalFilename = noteFile.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                if(extension.equals(".sql")){
                    newFileName = newNoteId+ ".txt";
                }else{
                    newFileName = newNoteId+ extension;
                }
                Path filePath = dirPath.resolve(newFileName);
                Files.write(filePath, noteFile.getBytes());
            }
        }
        return newFileName + ","; 
    }
    // 更新留言
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
        @PathVariable Integer id,
        @RequestParam("noteItem") String noteItem,
        @RequestParam("noteContent") String noteContent,
        @RequestParam(value = "noteFileImg", required = false) MultipartFile file,
        @RequestParam("noteFileText")String noteFileText,
        @RequestParam("noteDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime noteDate
    ){
        // 儲存檔案到本地目錄  
        System.out.println(noteFileText);
        return noteRepository.findById(id)
                .map(note -> {
                    note.setNoteItem(noteItem);
                    note.setNoteContent(noteContent);
                    note.setNoteDate(noteDate);
                    Optional.ofNullable(file).ifPresent(f -> saveFile(note.getNoteId(), f));
                    if(noteFileText != null && !noteFileText.isEmpty()){
                        Path dirPath = Paths.get(noteFilePath);
                        Path filePath = dirPath.resolve(note.getNoteId()+".txt");
                        try {
                            Files.write(
                                filePath,
                                noteFileText.getBytes(StandardCharsets.UTF_8)
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        noteRepository.findById(note.getNoteId()).ifPresent(note1 ->{
                            note1.setNoteFile(note1.getNoteFile()+","+ note.getNoteId()+".txt");
                            noteRepository.save(note1);
                        });
                    }
                    Note savedNote = noteRepository.save(note);
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

    //上傳圖檔
    public void saveFile ( Integer noteId, MultipartFile file){      
        String noteFile = "";
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename == null ? "txt" : originalFilename.substring(originalFilename.lastIndexOf('.')+1);
        extension = extension.equals("sql")  ? "txt" : extension ;
        String fileName = noteId.toString()+"."+extension;
        Path filePath = Paths.get(noteFilePath+"\\" , fileName);
        noteFile = noteFile + (noteFile.equals("") ? fileName : (","+fileName));
        System.out.println(noteFile);
        try {
            Files.createDirectories(filePath.getParent()); // 確保目錄存在
            Files.write(filePath, file.getBytes());    
        } catch (IOException e) {
            System.out.println(e);
        } 
        final String finalNoteFile = noteFile;
        noteRepository.findById(noteId).ifPresent(note ->{
            note.setNoteFile(finalNoteFile);
            noteRepository.save(note);
        }); 
    }


    //下載檔案
    @GetMapping("/download.do")
    public void download(@RequestParam String fileName,HttpServletResponse response,HttpServletRequest request) {        
        File file = new File (noteFilePath+File.separator+fileName);             
        if (!file.exists() || ! new File(noteFilePath).isDirectory()){
            System.out.println("檔案或目錄不存在");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }
        try (FileInputStream fis = new FileInputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())){
            String contentType = request.getServletContext().getMimeType(fileName);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            if (contentType.startsWith("text/")) {
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            }
            response.setContentType(contentType);                    
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
     public ResponseEntity<Map<String, Object>> readFile(@PathVariable Integer id) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isEmpty()){
            return ResponseEntity.notFound().build();
        }   
        Note note = optionalNote.get();
        String noteFile = note.getNoteFile();
        String[] fileNames = (noteFile == null || noteFile.isEmpty()) ? new String[0] :noteFile.split(",");
        Map <String,Object> response = new HashMap<>();
        for(String fileName:fileNames){
            if(fileName.endsWith(".jpg")  || fileName.endsWith(".jpge")|| fileName.endsWith(".png")){
                response.put("imageFile", loadFileAsBase64(fileName));
            }else{
                response.put("textFile", loadFileAsBase64(fileName));
            }
        }
        return ResponseEntity.ok(response);        
     }   
    private Map<String, String> loadFileAsBase64(String fileName) {
        try {
            File file = new File(noteFilePath + File.separator + fileName);
            byte[] bytes = Files.readAllBytes(file.toPath());

            Map<String, String> fileInfo = new HashMap<>();
            fileInfo.put("name", fileName);
            fileInfo.put("type", Files.probeContentType(file.toPath()));
            fileInfo.put("content", Base64.getEncoder().encodeToString(bytes));

            return fileInfo;

        } catch (Exception e) {
            return null;
        }
    }

}
