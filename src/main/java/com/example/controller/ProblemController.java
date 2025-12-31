package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Problem;
import java.util.Base64;

import com.example.config.ProblemRepository;

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
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemRepository problemRepository;
    private String problemFilePath = "C:\\DataSource\\problemfile";

    // 查全部
    @GetMapping("/index")
    public List<Problem> getAllProblems() {
        return problemRepository.findAll(Sort.by(Sort.Direction.DESC,"problemDate"));
    }

    // 依 ID 查
    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Integer id) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        return optionalProblem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 查某個使用者的留言
    @GetMapping("/item/{item}")
    public List<Problem> getProblemsByUserId(@PathVariable String item) {
        return problemRepository.findByProblemItem(item);
    }

    // 新增留言
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Problem createProblem(
        @RequestParam("problemItem") String problemItem,
        @RequestParam("problemContent") String problemContent,
        @RequestParam(value = "problemFile", required = false) MultipartFile[] files,
        @RequestParam("problemDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime problemDate
    ){ 
          System.out.println("!!!");
        // 建立 Problem 實體
        Problem problem = new Problem();
        problem.setProblemItem(problemItem);
        problem.setProblemContent(problemContent);
        problem.setProblemDate(problemDate);
        problemRepository.save(problem);
        Optional.ofNullable(files).ifPresent(f -> saveFile(problem.getProblemId(), f));
        return problem;
    }

    // 更新留言
    @PutMapping("/{id}")
    public ResponseEntity<Problem> updateProblem(
        @PathVariable Integer id,
        @RequestParam("problemItem") String problemItem,
        @RequestParam("problemContent") String problemContent,
        @RequestParam(value = "problemFile", required = false) MultipartFile[] files,
        @RequestParam("problemDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime problemDate) {
            // 儲存檔案到本地目錄  
        System.out.println("!!!");
        return problemRepository.findById(id)
                .map(problem -> {
                    problem.setProblemItem(problemItem);
                    problem.setProblemContent(problemContent);
                    problem.setProblemDate(problemDate);
                    Problem savedProblem = problemRepository.save(problem);
                    System.out.println(files.length);
                    Optional.ofNullable(files).ifPresent(f -> saveFile(problem.getProblemId(), f));
                    return ResponseEntity.ok(savedProblem);
                })
                .orElse(ResponseEntity.notFound().build()); 
    }

    // 刪除留言
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProblem(@PathVariable Integer id) {    
        if (problemRepository.existsById(id)) {
            System.out.println("刪除");//problemRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("刪除成功");
        }
        return ResponseEntity.status(HttpStatus.OK).body("刪除失敗");
    }

    //上傳檔案
    public void saveFile ( Integer problemId, MultipartFile[] files){
        System.out.println(files.length);
        int count =1;
        String problemFile = "";
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename == null ? "txt" : originalFilename.substring(originalFilename.lastIndexOf('.')+1);
            extension = extension.equals("sql")  ? "txt" : extension ;
            String fileName = problemId.toString()+"_"+count+"."+extension;
            Path filePath = Paths.get(problemFilePath+"\\" , fileName);
            problemFile = problemFile + (problemFile.equals("") ? fileName : (","+fileName));
            System.out.println(problemFile);
            try {
                Files.createDirectories(filePath.getParent()); // 確保目錄存在
                Files.write(filePath, file.getBytes());    
            } catch (IOException e) {
                System.out.println(e);
            } 
            count++;
        }
        final String finalProblemFile = problemFile;
        problemRepository.findById(problemId).ifPresent(problem ->{
            problem.setProblemFile(finalProblemFile);
            problemRepository.save(problem);
        });
    }

    //下載檔案
    @GetMapping("/download.do")
    public void download(@RequestParam String fileName,HttpServletResponse response,HttpServletRequest request) {        
        File file = new File (problemFilePath+File.separator+fileName);             
        if (!file.exists() || ! new File(problemFilePath).isDirectory()){
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
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isEmpty()){
            return ResponseEntity.notFound().build();
        }   
        Problem problem = optionalProblem.get();
        String problemFile = problem.getProblemFile();
        String[] fileNames = (problemFile == null || problemFile.isEmpty()) ? new String[0] :problemFile.split(",");
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
            File file = new File(problemFilePath + File.separator + fileName);
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
