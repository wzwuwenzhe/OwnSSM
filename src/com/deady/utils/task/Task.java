package com.deady.utils.task;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.deady.entity.operator.Operator;
import com.deady.service.OrderService;
import com.deady.utils.OperatorSessionInfo;
import com.deady.utils.SpringContextUtil;

public abstract class Task implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(Task.class);

	@Override
	public void run() {
		try {
			this.execute();
		} catch (Exception e) {
			logger.error("任务执行时发生错误", e);
		} finally {
		}

	}

	public abstract void execute() throws Exception;
}
