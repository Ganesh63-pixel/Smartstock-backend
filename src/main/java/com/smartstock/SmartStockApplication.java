package com.smartstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
@EnableScheduling
@SpringBootApplication
@ComponentScan({"com.smartstock.service", "com.smartstock.controller" ,"com.smartstock.util"})
@ComponentScan(basePackages = "com.smartstock")
@EnableJpaRepositories(basePackages = "com.smartstock.repository")
@EntityScan(basePackages = "com.smartstock.models")
public class SmartStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartStockApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
	    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
	    factory.setConnectTimeout(5000); // Set timeout for slow responses
	    factory.setReadTimeout(5000);

	    RestTemplate restTemplate = new RestTemplate(factory);

	    // Set default headers
	    restTemplate.getInterceptors().add((request, body, execution) -> {
	        request.getHeaders().set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
	        return execution.execute(request, body);
	    });

	    return restTemplate;
	}

}
