package com.microservices.msfour;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("MSFive")
@RibbonClient("MSFive")
public interface MSFiveServiceProxy {
		@RequestMapping("/greetingFromMSFive")
		String greeting();
}
