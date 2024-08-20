package com.cmwebgame.entities.vo;

import com.cmwebgame.entities.Employee;

public class EmployeeV extends Employee {
	
	private String departmentName;
	
	private String companyName;
	
	private String positionName;
	
	private String functionsName;
	
	private Long projectCount;

	public String getFunctionsName() {
		return functionsName;
	}

	public void setFunctionsName(String functionsName) {
		this.functionsName = functionsName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public Long getProjectCount() {
		return projectCount;
	}

	public void setProjectCount(Long projectCount) {
		this.projectCount = projectCount;
	}
	
	
}
