package com.cmwebgame.entities;

import java.sql.Timestamp;

import com.cmwebgame.dao.IdEntity;

public class Project extends IdEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String description;
	
	private Timestamp createTime;
	
	private String isBusiness;//是否是业务项目
	/**
	 * 特别指定算法，两种:按人数分摊，按业务业绩比例分摊；
	 * 默认为空，只有当项目属于非业务项目时才需要填写
	 */
	private String specialAlgorithm = "";
	
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
