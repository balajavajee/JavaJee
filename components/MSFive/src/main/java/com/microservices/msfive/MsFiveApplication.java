package com.microservices.msfive;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@RestController
public class MsFiveApplication {

	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	private MSOneServiceProxy msOneServiceProxy;
	private static final Logger logger = LoggerFactory.getLogger(MsFiveApplication.class);

	public static void main(String[] args) {
		logger.info(" IN MSFive application");
		SpringApplication.run(MsFiveApplication.class, args);
	}

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	@RequestMapping("/greetingFromMSFive")
	public String greeting() {
		return "Greetings from MSFive applications!";
	}

	@RequestMapping("/greetingFromMSOne")
	public String greetingFromMSOne() {
		return msOneServiceProxy.greetingFromMSOne();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
