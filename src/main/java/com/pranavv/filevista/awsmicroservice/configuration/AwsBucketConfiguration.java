package com.pranavv.filevista.awsmicroservice.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsBucketConfiguration {

    private String endPoint = "https://s3.ap-south-1.amazonaws.com";


    @Value("${awsBucketSecretKey}")
    private String secretKey;

    @Value("${awsBucketAccessKey}")
    private String accessKey;

    @Value("${awsRegion}")
    private String region;

    @Bean
    public AmazonS3 configureToBucket(){

        AWSCredentials awsCred = new BasicAWSCredentials(accessKey,secretKey);


        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCred))
                .build();

        return s3Client;

    }


}
