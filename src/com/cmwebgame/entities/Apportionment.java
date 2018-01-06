package com.cmwebgame.entities;

import com.cmwebgame.dao.IdEntity;

/**
 * 成本分摊模块
 * @author wilson
 *
 */
public class Apportionment extends IdEntity {
	
	private String name;//成本分摊名称
	
	private Long projectId;//所属项目
	
	private Long companyId;
	
	private String projectName;//项目名称
	
	private Double amount;//分摊数额
	
	private String month;//所属月份
	
	private String fromExcel;

	public String getFromExcel() {
		return fromExcel;
	}

	public void setFromExcel(String fromExcel) {
		this.fromExcel = fromExcel;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

}
