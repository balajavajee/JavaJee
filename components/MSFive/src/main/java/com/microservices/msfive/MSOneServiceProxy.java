package com.microservices.msfive;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name ="MSOne")
@RibbonClient("MSOne")
public interface MSOneServiceProxy {
		@RequestMapping("/greetingFromMSOne")
		String greetingFromMSOne();
}
