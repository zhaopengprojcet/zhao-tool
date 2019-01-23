package org.zhao.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/")
public class TestController {

	@RequestMapping("test.html")
	public String test() {
		//MessageUtil.sendMsg("test", "测试推送", MqTypeEm.SINGLE);
		return "success";
	}
}
