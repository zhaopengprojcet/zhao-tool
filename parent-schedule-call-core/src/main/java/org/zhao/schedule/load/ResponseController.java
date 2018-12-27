package org.zhao.schedule.load;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.schedule.model.ReturnCode;
import org.zhao.schedule.model.ScheduleModel;

@RestController
@RequestMapping("/schedule/")
public class ResponseController {

	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping("response.html")
	public String response(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(StringUtils.isEmpty(context)) return ReturnCode.ERROR;
		logger.info("schedule请求参数【"+context+"】");
		try {
			JSONObject obj = JSONObject.fromObject(context);
			if(!obj.containsKey("_tk") || !obj.containsKey("_sev")) 
				return ReturnCode.ERROR;
			String token = obj.getString("_tk");
			if(RegiestServer.getToken(false).equals("-1")) {
				logger.info("schedule请求无效，当前服务未进行注册");
				return ReturnCode.error("schedule请求无效，当前服务未进行注册");
			}
			if(!RegiestServer.getToken(false).equals(token)) {
				logger.info("schedule请求无效，token错误【"+RegiestServer.getToken(false)+"】【"+token+"】");
				return ReturnCode.error("schedule请求无效，token错误");
			}
			String service = obj.getString("_sev");
			if(!ServletScheduleLoadInit.getSchedules().containsKey(service)) {
				logger.info("schedule请求无效，未注册【"+service+"】");
				return ReturnCode.error("schedule请求无效，未注册服务");
			}
			ScheduleModel model = ServletScheduleLoadInit.getSchedules().get(service);
			
			Object cla = applicationContext.getBean(model.getCla().getClass());
			return JSONObject.fromObject(model.getMethod().invoke(cla)).toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ReturnCode.ERROR;
	}
}
