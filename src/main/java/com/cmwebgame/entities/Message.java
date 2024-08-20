package com.cmwebgame.entities;

import java.sql.Timestamp;

import com.cmwebgame.dao.IdEntity;

public class Message extends IdEntity {
	
	private String title;
	
	private String content;
	
	private String Kind;//消息对象
	
	private Long kindId;//消息对象Id
	
	private String isReaded;//是否已读
	
	private Timestamp updateTime;
	
	private Timestamp createTime;
	
	public String getIsReaded() {
		return isReaded;
	}

	public void setIsReaded(String isReaded) {
		this.isReaded = isReaded;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getKind() {
		return Kind;
	}

	public void setKind(String kind) {
		Kind = kind;
	}

	public Long getKindId() {
		return kindId;
	}

	public void setKindId(Long kindId) {
		this.kindId = kindId;
	}
	
	

}
