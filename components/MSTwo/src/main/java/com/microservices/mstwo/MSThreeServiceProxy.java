package com.microservices.mstwo;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("MSThree")
@RibbonClient("MSThree")
public interface MSThreeServiceProxy {
		@RequestMapping("/greetingFromMSThree")
		String greeting();
}
