function schedule_type(v ,r) {
	if(v == "FIXTIME") return "固定时间";
	if(v == "FIXWEEK") return "固定星期时间";
	if(v == "FIXDAY") return "固定天时间";
	if(v == "EC") return "固定间隔分钟";
	if(v == "FUSH") return "推送轮询";
}

function schedule_put_type(v , r) {
	if(v == "ALL") return "全部调用";
	if(v == "ONLY") return "单服务调用";
}