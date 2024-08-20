package com.cmwebgame.entities;

import com.cmwebgame.dao.IdEntity;

public class ModuleContent extends IdEntity {
	
	private String name;
	
	private Long moduleId;
	/**
	 * 模块的url地址
	 */
	private String url;
	/**
	 * 所需访问权限
	 */
	private String permission;
	/**
	 * 模块描述
	 */
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
