package org.zhao.usetime.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadExectionHandler implements Thread.UncaughtExceptionHandler{

	private Log logger = LogFactory.getLog(ThreadExectionHandler.class);
	
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		logger.error("线程【"+arg0.getName()+"】异常【"+arg1.getMessage()+"】");
	}

}
