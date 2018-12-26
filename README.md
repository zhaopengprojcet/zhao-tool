# zhao-tool

common - 基础模版项目  
server-center 扩展为注册中心  
    1. 耗时监测  
    
use-time-core -耗时监测组件  
memory-cpu-core -计算机内存，cpu，磁盘占用监测组件

>>2018-12-26  
	弃用原有条件查询工具类  
	org.zhao.common.mybatis.query.ParamterRequirement ，  
	在原基础上更改为  
	org.zhao.common.mybatis.query.QueryParames  
	条件查询方式优化，支持单属性值多种条件判断  
  
>>2018-12-25  
	优化耗时记录表结构，分解记录主体为ip，请求服务，记录函数  
	优化项目组件结构，增加memory-cpu-core组件和监测接口
