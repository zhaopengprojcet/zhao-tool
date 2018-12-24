package org.zhao.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.properties.ConfigProperties;
import org.zhao.common.util.SessionUtil;

@Aspect
@Component
public class RequestAfterAspect {

 	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void cutResultController(){
    }
 	@Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void cutController(){
    }
 	/**
 	 * 加密接口返回值
 	 * @param joinPoint
 	 * @return
 	 * @throws Throwable
 	 */
 	@Around("cutController()")
 	public Object cutController(ProceedingJoinPoint joinPoint) throws Throwable {
 		return doController(joinPoint);
 	}
 	@Around("cutResultController()")
 	public Object cutResultController(ProceedingJoinPoint joinPoint) throws Throwable {
 		return doController(joinPoint);
 	}
 	
 	private Object doController(ProceedingJoinPoint joinPoint) throws Throwable {
 		String classType = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
 		long begin = System.currentTimeMillis();
 		Object obj= joinPoint.proceed();
 		long end = System.currentTimeMillis();
 		String name = "【" + SessionUtil.getWebIp() + "--->" + ConfigProperties.instance().getPropertiesVal("run.server.name") + "】" + classType + "." + methodName;
 		QueryTimeUse.putTime(name, end - begin);
 		
 		return obj;
 	}
}
