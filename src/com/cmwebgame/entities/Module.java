package com.cmwebgame.entities;

import com.cmwebgame.dao.IdEntity;

public class Module extends IdEntity {
	/**
	 * 模块名称
	 */
	private String name;
	/**
	 * 模块的url地址
	 */
	private String url;
	/**
	 * 父模块Id
	 */
	private Long fatherModuleId;
	/**
	 * icon图标字符串
	 */
	private String icon;
	/**
	 * 模块描述
	 */
	private String description;
	/**
	 * 排序
	 */
	private int sort;

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getFatherModuleId() {
		return fatherModuleId;
	}

	public void setFatherModuleId(Long fatherModuleId) {
		this.fatherModuleId = fatherModuleId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
