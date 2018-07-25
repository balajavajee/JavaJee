package com.microservices.mstwo;

import org.springframework.beans.factory.annotation.Autowired;
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
public class MsTwoApplication {

	@Autowired
	private MSThreeServiceProxy msThreeServiceProxy;

	public static void main(String[] args) {
		SpringApplication.run(MsTwoApplication.class, args);
	}

	@RequestMapping("/greetingFromMSTwo")
	public String greeting() {
		return "Greetings from MSTwo appls!";
	}

	@RequestMapping("/greetingFromMSThree")
	public String greetingFromMSThree() {
		return msThreeServiceProxy.greetingFromMSThree();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
