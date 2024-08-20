package com.cmwebgame.entities;

import java.sql.Timestamp;

import com.cmwebgame.dao.IdEntity;

/**
 * 职能，如：专员、主管、经理
 * @author wilson
 *
 */
public class Position extends IdEntity {
	
	private String name;
	
	private String description;
	
	
	private Timestamp createTime;
	

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
