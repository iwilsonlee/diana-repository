package com.cmwebgame.entities;

import com.cmwebgame.dao.IdEntity;

/**
 * 人员权限关系
 * @author wilson
 *
 */
public class Authority extends IdEntity {
	
	private Long employeeId;
	
	private Long moduleId;
	
	private String permission;//权限
	
	private String description;
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	

}
