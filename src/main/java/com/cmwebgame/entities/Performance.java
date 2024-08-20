package com.cmwebgame.entities;

import java.sql.Timestamp;
import java.util.Date;

import com.cmwebgame.dao.IdEntity;

/**
 * 项目业绩
 * @author wilson
 *
 */
public class Performance extends IdEntity {
	
	private Long projectId;
	
	private Double amount;//业绩金额
	
	private Timestamp createTime;
	
	private String month;//所属月份

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	
}
