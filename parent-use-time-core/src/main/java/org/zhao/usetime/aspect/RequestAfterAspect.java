package org.zhao.usetime.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.zhao.core.common.util.ThreadPoolUtils;
import org.zhao.usetime.model.PutThread;

@Aspect
public class RequestAfterAspect {
	
 	@Pointcut("@annotation(org.zhao.usetime.annotation.UseTime) ")
    public void cutUseTimeMethod(){
    }
 	/**
 	 * 加密接口返回值
 	 * @param joinPoint
 	 * @return
 	 * @throws Throwable
 	 */
 	@Around("cutUseTimeMethod()")
 	public Object cutUseTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
 		String classType = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
 		long begin = System.currentTimeMillis();
 		Object obj= joinPoint.proceed();
 		long end = System.currentTimeMillis();
 		try {
			String name = classType + "." + methodName;
			ThreadPoolUtils.putThread("use-time-put", new PutThread(name , end-begin));
 		} catch (Exception e) {
			e.printStackTrace();
		}
 		return obj;
 	}
 	
}
