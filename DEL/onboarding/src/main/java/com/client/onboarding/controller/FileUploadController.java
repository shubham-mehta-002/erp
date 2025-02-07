package com.client.onboarding.controller;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.http.HttpHeaders;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;


    @PostMapping
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        Map<String, String> response = new HashMap<>();
    
        if (file.isEmpty()) {
            response.put("error", "No file uploaded");
            return response;
        }
    
        try {
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }
    
            // Generate unique file name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName).toAbsolutePath().normalize();
            File destFile = filePath.toFile();
    
            // Save the file
            file.transferTo(destFile);
    
            // Return the **local system file path** instead of an API URL
            response.put("fileUrl", filePath.toString()); // Absolute file path
        } catch (IOException e) {
            response.put("error", "File upload failed: " + e.getMessage());
        }
    
        return response;
    }
    
// @GetMapping("/api/files/download/{fileName}")
//     public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws MalformedURLException {
//     Path filePath = Paths.get("C:\\Users\\Shubham Mehta\\Desktop\\pisoft_tasks\\client_onboarding_task\\DEL\\assets")
//                         .resolve(fileName).normalize();
//     Resource resource = new UrlResource(filePath.toUri());

//     if (!resource.exists() || !resource.isReadable()) {
//         return ResponseEntity.notFound().build();
//     }

//     return ResponseEntity.ok()
//         .contentType(MediaType.APPLICATION_OCTET_STREAM)
//         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//         .body(resource);
// }


    // @PostMapping
    // public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
    //     Map<String, String> response = new HashMap<>();
        
    //     if (file.isEmpty()) {
    //         response.put("error", "No file uploaded");
    //         return response;
    //     }

    //     try {
    //         File uploadDirectory = new File(uploadDir);
    //         if (!uploadDirectory.exists()) {
    //             uploadDirectory.mkdirs(); 
    //         }
    //         String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    //         Path filePath = Paths.get(uploadDir, fileName);
    //         File destFile = filePath.toFile();
    //         file.transferTo(destFile);
    //         response.put("fileUrl", "/api/files/download/" + fileName);
    //     } catch (IOException e) {
    //         response.put("error", "File upload failed: " + e.getMessage());
    //     }

    //     return response;
    // }


    
}
