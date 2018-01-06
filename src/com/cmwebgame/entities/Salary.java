package com.cmwebgame.entities;

import com.cmwebgame.dao.IdEntity;

/**
 * 人员基本薪资模块
 * @author wilson
 *
 */
public class Salary extends IdEntity {
	
	private Long employeeId;
	
	private double amount;
	
	private String month;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
}
