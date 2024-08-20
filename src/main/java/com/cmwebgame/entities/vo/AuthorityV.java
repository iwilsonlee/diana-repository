package com.cmwebgame.entities.vo;

import com.cmwebgame.entities.Authority;

public class AuthorityV extends Authority {
	
	private String positionMemo;
	
	private String employeeName;
	
	private String moduleName;
	
	private String moduleUrl;
	
	private String moduleIcon;
	
	private Long fatherModuleId;
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getFatherModuleId() {
		return fatherModuleId;
	}

	public void setFatherModuleId(Long fatherModuleId) {
		this.fatherModuleId = fatherModuleId;
	}

	public String getModuleIcon() {
		return moduleIcon;
	}

	public void setModuleIcon(String moduleIcon) {
		this.moduleIcon = moduleIcon;
	}


	public String getPositionMemo() {
		return positionMemo;
	}

	public void setPositionMemo(String positionMemo) {
		this.positionMemo = positionMemo;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}
	
	

}
