package com.cmwebgame.entities;

import com.cmwebgame.dao.IdEntity;

/**
 * 项目-人员 关系
 * @author wilson
 *
 */
public class ProjectEmployee extends IdEntity {
	
	private Long projectId;
	
	private Long employeeId;
	
//	private Long functionsId;
	
	private double apportion; //分摊
	
	private String month;//所属月份

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

//	public Long getFunctionsId() {
//		return functionsId;
//	}
//
//	public void setFunctionsId(Long functionsId) {
//		this.functionsId = functionsId;
//	}

	public double getApportion() {
		return apportion;
	}

	public void setApportion(double apportion) {
		this.apportion = apportion;
	}
	
	

}
