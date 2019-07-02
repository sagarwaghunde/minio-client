package org.saga.sample.webapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;

import org.saga.sample.webapp.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UploadController {
    
    @Autowired
    private UploadFileService uploadFileService;
    
    @GetMapping(path = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getHelloWorld() {
        return "Hello World";
    }
    
    @GetMapping(path = "/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable("filename") String filename) throws IOException {
    	byte[] content = uploadFileService.downloadFile(filename);
        return ResponseEntity.ok()
                .contentLength(content.length)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "downloadedFile")
                .body(content);
    }
    
    
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException, NoSuchAlgorithmException {
        return new ResponseEntity<String>("File uploaded successfully. Path : " + uploadFileService.uploadFile(file), HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/upload-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> uploadDetails(@RequestParam("file") MultipartFile file, @RequestParam("user") String user, HttpServletRequest request) throws IllegalStateException, IOException, NoSuchAlgorithmException {
    	System.out.println(user);
        String path = uploadFileService.uploadFile(file);
        return new ResponseEntity<String>("File uploaded successfully. Path : " + path, HttpStatus.CREATED);
    }
}
