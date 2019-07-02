package org.saga.sample.webapp.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.saga.sample.webapp.client.AwsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {

    public static final String FILE_URL = "/usr/local/proad/application/EC/dummy";
    public static final String MINIO = "http://localhost:9000/minio/demo";
    
    @Autowired
    private AwsClient awsClient;
    
    public String uploadFile(MultipartFile file) throws IllegalStateException, IOException, NoSuchAlgorithmException {
        Path filePath = Paths.get(FILE_URL + "/" + file.getOriginalFilename());
        file.transferTo(filePath);
        String fileName = this.generateFilename(file.getOriginalFilename());
        awsClient.uploadFile(fileName, new File(filePath.toString()));
        Files.delete(filePath);
        return fileName;
    }
    
    public String uploadDetails(MultipartFile file) throws IllegalStateException, IOException {
        Path filePath = Paths.get(FILE_URL + "/" + file.getOriginalFilename());
        file.transferTo(filePath);
        return FILE_URL + "/" + file.getOriginalFilename();
    }
    
    public byte[] downloadFile(String filename) throws IOException {
    	InputStream in = this.awsClient.getFile(filename).getObjectContent();
    	byte[] contents = new byte[in.available()];
    	in.read(contents);
    	return contents;
    }
    
    public String generateFilename(String filename) throws IOException {
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            return filename;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        byte[] filenameBytes = filename.getBytes();
        byte[] messageDigest = instance.digest(String.valueOf(System.nanoTime()).getBytes());
        outputStream.write(messageDigest);
        outputStream.write(filenameBytes);
        StringBuilder hexString = new StringBuilder();
        	byte[] outputStreamBytes = outputStream.toByteArray();
        for (int i = 0; i < outputStreamBytes.length; i++) {
            String hex = Integer.toHexString(0xFF & outputStreamBytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
