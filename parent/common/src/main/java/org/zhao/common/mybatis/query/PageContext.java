package org.zhao.common.mybatis.query;

import java.io.Serializable;

public class PageContext implements Serializable {
	private static final long serialVersionUID = -1649724077L;
	private Integer start;
	private Integer count;
	private String order;
	private String orderBy;
	
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";

	public PageContext(Integer pageNum, Integer count, String order) {
		this.start = Integer.valueOf((pageNum.intValue() - 1)
				* count.intValue());
		this.count = count;
		if(pageNum == -1) {
			this.start = null;
			this.count = null;
		}
		this.order = order;
	}
	
	public PageContext(Integer start, Integer count, String order,
			String orderBy) {
		this.start = Integer.valueOf((start.intValue() - 1) * count.intValue());
		this.count = count;
		if(start == -1) {
			this.start = null;
			this.count = null;
		}
		this.order = order;
		this.orderBy = orderBy;
	}

	public PageContext(Integer pageNum, Integer count) {
		
		this.start = Integer.valueOf((pageNum.intValue() - 1)
				* count.intValue());
		this.count = count;
		if(pageNum == -1) {
			this.start = null;
			this.count = null;
		}
	}

	public PageContext(String order ,String orderBy) {
		this.start = null;
		this.count = null;
		this.order = order;
		this.orderBy = orderBy;
	}
	
	@Override
	public String toString() {
		return this.order + this.orderBy+this.count.intValue()+this.start.intValue();
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getOrder() {
		return this.order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
