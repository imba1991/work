package com.ht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qiujunda
 * @date 2019年1月2日
 * @description
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
@EntityScan
@SpringBootApplication
@EnableCaching
@EnableEurekaClient
public class UserCenterStartBootApplication {
	
	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(UserCenterStartBootApplication.class, args);
		
	}
}
