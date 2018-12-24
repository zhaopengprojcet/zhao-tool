package org.zhao.common.exceptionListen;

import org.apache.log4j.Logger;

public class ThreadExectionHandler implements Thread.UncaughtExceptionHandler{

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		logger.error("线程【"+arg0.getName()+"】异常【"+arg1.getMessage()+"】");
	}

}
