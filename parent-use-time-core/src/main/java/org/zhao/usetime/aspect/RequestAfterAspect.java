package org.zhao.usetime.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.zhao.core.common.model.PutThread;
import org.zhao.core.common.util.ThreadPoolUtils;

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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("from", name);
			map.put("times", end-begin);
			ThreadPoolUtils.putThread("use-time-put", new PutThread(map , "/server/putUseTime.html"));
 		} catch (Exception e) {
			e.printStackTrace();
		}
 		return obj;
 	}
 	
}
