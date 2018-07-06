package com.microservices.msthree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@RestController
public class MsThreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsThreeApplication.class, args);
	}
	@RequestMapping("/greetingFromMSThree")
    public String greeting() {
        return "Greetings from MSThree applications!";
    }

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
