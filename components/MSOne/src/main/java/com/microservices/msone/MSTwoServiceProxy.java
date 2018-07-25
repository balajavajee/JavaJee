package com.microservices.msone;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("MSTwo")
@RibbonClient("MSTwo")
public interface MSTwoServiceProxy {
		@RequestMapping("/greetingFromMSTwo")
		String greetingFromMSTwo();
}
