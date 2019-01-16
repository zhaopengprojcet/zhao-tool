package org.zhao.controller;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhao.common.zmq.queue.ZQueueConfig;
import org.zhao.common.zmq.queue.Zqueue;
import org.zhao.mq.annotation.em.MqTypeEm;
import org.zhao.mq.util.MessageUtil;

@RestController
@RequestMapping("/test/")
public class TestController {

	@RequestMapping("test.html")
	public String test() {
		MessageUtil.sendMsg("test", "测试推送", MqTypeEm.SINGLE);
		return "success";
	}
}
