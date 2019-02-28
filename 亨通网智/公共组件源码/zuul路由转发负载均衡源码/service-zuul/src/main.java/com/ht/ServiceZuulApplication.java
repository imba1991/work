package com.ht;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
@EntityScan
@EnableDiscoveryClient
public class ServiceZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceZuulApplication.class, args);
	}

//	@Value("${server.port}")
//	String port;
//	@RequestMapping("/hi")
//	public String home(@RequestParam String name) {
//		System.out.println("请求的name"+name);
//		return "hi welcome to zuul "+name+",i am from port:" +port;
//	}
//
//	@Bean
//	public LogFilter logFilter() {
//		return new LogFilter();
//	}
//
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory=new MultipartConfigFactory();
		factory.setMaxFileSize("10MB");
		factory.setMaxRequestSize("50MB");
		String location=System.getProperty("user.dir")+"/data/tmp";
		File tmpFile=new File(location);
		if(!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		factory.setLocation(location);
		System.out.println("---------------"+location);
		return factory.createMultipartConfig();
	}
}
