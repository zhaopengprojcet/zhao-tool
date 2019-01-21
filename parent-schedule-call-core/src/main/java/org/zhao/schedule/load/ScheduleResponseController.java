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
import org.zhao.core.common.model.ReturnCode;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.ThreadPoolUtils;
import org.zhao.schedule.model.ScheduleModel;
import org.zhao.schedule.thread.ScheduleDo;

@RestController
@RequestMapping("/schedule/")
public class ScheduleResponseController {

	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping("response.html")
	public String response(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(StringUtils.isEmpty(context)) return ReturnCode.ERROR;
		logger.info("schedule请求参数【"+context+"】");
		try {
			JSONObject obj = JSONObject.fromObject(context);
			// token  , sericeName   scheduleId
			if(!obj.containsKey("_tk") || !obj.containsKey("_sev") || !obj.containsKey("_sci") || !obj.containsKey("_wl")) 
				return ReturnCode.ERROR;
			String token = obj.getString("_tk");
			if(RegiestServer.getToken(false ,ScheduleServletScheduleLoadInit.SCC_KEY).equals("-1")) {
				logger.info("schedule请求无效，当前服务未进行注册");
				return ReturnCode.success("schedule请求无效，当前服务未进行注册");
			}
			if(!RegiestServer.getToken(false ,ScheduleServletScheduleLoadInit.SCC_KEY).equals(token)) {
				logger.info("schedule请求无效，token错误【"+RegiestServer.getToken(false ,ScheduleServletScheduleLoadInit.SCC_KEY)+"】【"+token+"】");
				return ReturnCode.success("schedule请求无效，token错误");
			}
			String service = obj.getString("_sev");
			if(!ScheduleServletScheduleLoadInit.getSchedules().containsKey(service)) {
				logger.info("schedule请求无效，未注册【"+service+"】");
				return ReturnCode.success("schedule请求无效，未注册服务");
			}
			if(StringUtils.isEmpty(obj.getString("_sci"))) {
				logger.info("schedule请求无效，未分派任务编码");
				return ReturnCode.success("schedule请求无效，未分派任务编码");
			}
			
			ScheduleModel model = ScheduleServletScheduleLoadInit.getSchedules().get(service);
			
			ThreadPoolUtils.putThread("schedule-do【"+obj.getString("_sci")+"】", new ScheduleDo(obj.getString("_sci"), model, applicationContext , obj.getBoolean("_wl")));
			
			return ReturnCode.SUCCESS; //返回调用状态，不包括执行状态
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ReturnCode.ERROR;
	}
}
