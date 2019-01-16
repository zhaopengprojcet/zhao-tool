package org.zhao.common.aspect.query;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.zhao.common.client.ClientContext;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.pojo.model.ZserverExpModel;
import org.zhao.service.ZserverExpService;

/**
 * 计算机异常接收 处理线程
 * @author zhao
 *
 */
public class QueryMcs extends Thread{

	private List<Double> datas;
	private ZserverExpService zServerExpService;
	private ClientContext client;
	
	public QueryMcs(){}
	public QueryMcs(List<Double> data , ZserverExpService zServerExpService , ClientContext client) {
		this.datas = data;
		this.zServerExpService = zServerExpService;
		this.client = client;
	}
	
	@Override
	public void run() {
		double cpu = PublicServerKV.getDoubleVal("server-center.mcs.cpu.max");
		double mem = PublicServerKV.getDoubleVal("server-center.mcs.memory.max");
		double disk = PublicServerKV.getDoubleVal("server-center.mcs.disk.max");
		
		
		double allMem = datas.get(0);//全部内存
		double useMem = datas.get(0) - datas.get(1);//已用内存
		double cpuUse = datas.get(2);//cpu使用率
		double diskUse = datas.get(3);//磁盘占用率
		
		String expKey = "";
		if((useMem / allMem * 100) >= mem) {
			double d = useMem / allMem * 100;
			BigDecimal b = new BigDecimal(d);
			d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			expKey += "---内存异常数值【"+d+"】指标【"+mem+"】---";
		}
		
		if(cpuUse >= cpu) {
			expKey += "---CPU异常数值【"+cpuUse+"】指标【"+cpu+"】---";
		}
		
		if(diskUse >= disk) {
			expKey += "---磁盘异常数值【"+diskUse+"】指标【"+disk+"】---";
		}
		
		if(!StringUtils.isEmpty(expKey)) {
			ZserverExpModel model = new ZserverExpModel();
			model.setExpDesc(expKey);
			model.setQueryIp(this.client.getIp());
			model.setQueryServerName(this.client.getServiceName());
			this.zServerExpService.save(model);
		}
	}
}
