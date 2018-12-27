# zhao-tool

common - 基础模版项目  
server-center 扩展为注册中心  
    1. 耗时监测  
    
use-time-core -耗时监测组件  
memory-cpu-core -计算机内存，cpu，磁盘占用监测组件  
schedule-call-core - 分布式定时任务调度组件  

>>待完成  
	服务端对定时任务指定周期的函数解析，预加载，  
	以及定时任务扫描时对当前时间点的命中逻辑，防止重复逻辑（流程等待确定）  
	对调用功能中的测试伪代码实现  
	  
	服务端增加定时任务制定功能（包括主动调度和定时调度逻辑，分离任务注册与任务执行逻辑）  
	服务端实现定时任务调度函数（调度请求的发起）   
	~~服务端定时任务扫描机制确认 (期望在未开启定时任务服务时不占用系统线程)~~     
	客户端定时任务调度结果回馈机制确认 (如何分离调度与返回结果，避免因客户端长时间任务导致超时)    
  
  
>>更新记录  
  
>2018-12-27  
	优化原有通用缓存工具org.zhao.common.util.CacheUtil实现  
	现可支持map<String,List<Object>>及 map<String,Set<Object>> 函数  
	在存过程中传入的value为object或集合形式  
	新增schedule-call-core组件  
	添加定时任务注册接口  
	`完成定时任务调度流程框架搭建和测试,测试通过` 
	`完成客户端任务调度接口的测试`  
	
>2018-12-26  
	弃用原有条件查询工具类  
	org.zhao.common.mybatis.query.ParamterRequirement ，  
	在原基础上更改为  
	org.zhao.common.mybatis.query.QueryParames  
	条件查询方式优化，支持单属性值多种条件判断  
	重写mybatis公用查询xml配置文件生成工具类用于支持新版查询工具  
	  
  
>2018-12-25  
	优化耗时记录表结构，分解记录主体为ip，请求服务，记录函数  
	优化项目组件结构，增加memory-cpu-core组件和监测接口
