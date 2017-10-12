package com.deady.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * 分页工具类
 * 
 * @author wuwz
 * 
 */
public class PageUtils {

	private int total;// 总数
	private int start;// 第几页
	private int pagesize;// 每页条数

	public PageUtils(HttpServletRequest req) {
		if (null == req) {
			return;
		}
		String start = req.getParameter("start");
		String pagesize = req.getParameter("pagesize");
		if (StringUtils.isEmpty(start)) {
			this.start = 1;
		} else {
			this.start = Integer.parseInt(start);
		}
		if (StringUtils.isEmpty(pagesize)) {
			this.pagesize = 10;
		} else {
			this.pagesize = Integer.parseInt(pagesize);
		}
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

}
