package com.microservices.msthree;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("MSFour")
@RibbonClient("MSFour")
public interface MSFourServiceProxy {
		@RequestMapping("/greetingFromMSFour")
		String greeting();
}
