package com.pranavv.filevista.awsmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@EnableFeignClients
public class AwsMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsMicroserviceApplication.class, args);
	}

}
