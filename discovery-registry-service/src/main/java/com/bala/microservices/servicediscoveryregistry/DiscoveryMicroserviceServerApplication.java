package com.bala.microservices.servicediscoveryregistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
@RestController
public class DiscoveryMicroserviceServerApplication {
	@Autowired
	private static DiscoveryClient discoveryClient;

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryMicroserviceServerApplication.class, args);
	}
/*
	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		System.out.println("Application instances:: " + this.discoveryClient.getInstances("MSFive"));
		return this.discoveryClient.getInstances(applicationName);
	}
*/}
