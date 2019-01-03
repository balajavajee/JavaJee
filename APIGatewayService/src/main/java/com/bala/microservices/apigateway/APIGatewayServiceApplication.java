package com.bala.microservices.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.web.bind.annotation.RestController;

@EnableZuulServer
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class APIGatewayServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(APIGatewayServiceApplication.class, args);
	}

}
