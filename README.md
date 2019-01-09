# zhao-tool

###### `项目提供自己做的一些方便工作中使用的插件，有时候因为需要使用功能而为了效率和时间成本，都回去网上直接找已经完成的三方工具.但是很多时候出现问题了处理起来就比较麻烦，甚至可能还需要更改源码,所以空闲时就弄这么一个项目集，把工作重用到的一些功能自己实现一套在后面的项目中如果还需要使用类似的三方工具时，就多了一个选择并且修改起来更简单`    

common - 基础模版项目  
server-center 扩展为注册中心  
    
use-time-core -耗时监测组件  
memory-cpu-core -计算机内存，cpu，磁盘占用监测组件  
schedule-call-core - 分布式定时任务调度组件  
  
>>待完成  
	~~实现任务调度功能中的手动任务调度~~   
	`实现任务调度功能中日志模块的页面处理`    
	`构思组件数据结果的图形化展示界面`  
	~~服务端对定时任务指定周期的函数解析，预加载,~~    
	~~以及定时任务扫描时对当前时间点的命中逻辑，防止重复逻辑（流程等待确定）~~    
	~~对调用功能中的测试伪代码实现~~    
	~~服务端增加定时任务制定功能（包括主动调度和定时调度逻辑，分离任务注册与任务执行逻辑）~~    
	~~服务端实现定时任务调度函数（调度请求的发起）~~     
	~~服务端定时任务扫描机制确认 (期望在未开启定时任务服务时不占用系统线程)~~     
	~~客户端定时任务调度结果回馈机制确认 (如何分离调度与返回结果，避免因客户端长时间任务导致超时)~~      
  
  
>>更新记录  
  
>2019-01-09  
	启动mq组件开发工作  
	完成服务端内存队列和redis队列初步原型开发  
	
>2019-01-02  
	实现手动任务调度功能  
	任务调度日志模块实现  
	延缓任务调度日志图形化界面，后移至后期统计模块统一做模块化处理  
	优化任务调度日志数据结构，方便后期做统计  
	缓存工具添加自定义缓存位置  
	完成任务调度组件客户端和服务端分时处理逻辑  
	
>2018-12-29  
	明天放假，今天无心工作^-^  
	添加定时任务客户端存活检测机制  
	添加定时任务服务端主动刷新自定义任务修改  
	调整组件manve至server-center项目  
	调整系统参数命名规范  
	修正多次注册任务可能导致token重复的问题  
	修复通用更新页面数据加载中easyu.min.js源码中导致页面数据加载完成后二次渲染导致数据丢失的问题  
	优化通用页面查询模块的参数  
	今天就这么多了！！！节后继续  
	
>2018-12-28  
	实现任务调度组件客户端和服务端初步处理逻辑及流程（已通，未严格测试）  
	实现服务端对任务调度的自定义设置，服务端对任务调度的日志记录，日子包含调度时间，调度任务，调度结果，任务执行结果，任务直接结果反馈时间，调度客户端信息等  
	
>2018-12-27  
	优化原有通用缓存工具org.zhao.common.util.CacheUtil实现  
	现可支持map<String,List<Object>>及 map<String,Set<Object>> 函数  
	在存过程中传入的value为object或集合形式  
	新增schedule-call-core组件  
	添加定时任务注册接口  
	`完成定时任务调度流程框架搭建和测试,测试通过` 
	`完成客户端任务调度接口的测试`  
	`添加core-common项目为组件提供统一基础工具支持，防止在多工具同时使用时类库的凌乱`  
	
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
