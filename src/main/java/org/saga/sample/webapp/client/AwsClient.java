package org.saga.sample.webapp.client;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class AwsClient {

	public static final String FILE_URL = "/usr/local/proad/application/EC/dummy";
	
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    
    @PostConstruct
    private void initializeAmazon() {
       AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
       this.s3client = AmazonS3ClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, "us-west-2"))
                                                           .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                                           .withPathStyleAccessEnabled(true)
                                                           .build();
    }
    
    public void uploadFile(String fileName, File file) {
        s3client.putObject(this.bucketName, fileName, file);
    }
    
    public S3Object getFile(String filename) throws IOException {
    	return s3client.getObject(this.bucketName, filename);
    }
}