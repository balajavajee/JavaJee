package com.microservices.msone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
@RestController
public class MsOneApplication {

	@Autowired
	private MSTwoServiceProxy msTwoServiceProxy;
	private static final Logger logger = LoggerFactory.getLogger(MsOneApplication.class);

	public static void main(String[] args) {
		logger.info(" IN MSFive application");
		SpringApplication.run(MsOneApplication.class, args);
	}

	@RequestMapping("/greetingFromMSOne")
	public String greetingFromMSOne() {
		return "Greetings from MSOne applications!";
	}

	@RequestMapping("/greetingFromMSTwo")
	public String greetingFromMSTwo() {
		return msTwoServiceProxy.greetingFromMSTwo();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
