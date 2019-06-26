package br.com.projetcworkshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${aws.access_key_id}")
	private String awsAccessKey;

	@Value("${aws.secret_access_key}")
	private String awsSecretKey;

	@Value("${s3.region}")
	private String regionAWS;

	@Bean
	public AmazonS3 s3Client() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(regionAWS)).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

		return s3Client;
	}
}