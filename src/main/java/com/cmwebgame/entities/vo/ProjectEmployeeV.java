package com.cmwebgame.entities.vo;

import com.cmwebgame.entities.ProjectEmployee;

public class ProjectEmployeeV extends ProjectEmployee {
	
	private String projectName;
	
	private String employeeName;
	
	private String isBusiness;//是否是业务项目，1是，0否
	
	private String departmentName;
	
	private String isFulltime;
	
	private String leaveJobDate;
	
	public String getLeaveJobDate() {
		return leaveJobDate;
	}

	public void setLeaveJobDate(String leaveJobDate) {
		this.leaveJobDate = leaveJobDate;
	}

	/**
	 * 特别指定算法，两种:按人数分摊，按业务业绩比例分摊
	 */
	private String specialAlgorithm;
	
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getIsFulltime() {
		return isFulltime;
	}

	public void setIsFulltime(String isFulltime) {
		this.isFulltime = isFulltime;
	}

	public String getSpecialAlgorithm() {
		return specialAlgorithm;
	}

	public void setSpecialAlgorithm(String specialAlgorithm) {
		this.specialAlgorithm = specialAlgorithm;
	}

	public String getIsBusiness() {
		return isBusiness;
	}

	public void setIsBusiness(String isBusiness) {
		this.isBusiness = isBusiness;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
