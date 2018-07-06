package com.microservices.apigatewayserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.web.bind.annotation.RestController;

@EnableZuulServer
@EnableZuulProxy
@SpringBootApplication
@RestController
public class APIGatewayServiceApplication {
	@Autowired
	SimpleFilter simpleFilter;

	public static void main(String[] args) {
		SpringApplication.run(APIGatewayServiceApplication.class, args);
	}

}
