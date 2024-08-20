package com.cmwebgame.entities;

import com.cmwebgame.dao.IdEntity;

/**
 * 成本类别模块
 * @author wilson
 *
 */
public class Cost extends IdEntity {
	
	private String name;//成本名称
	
	private String kind;//所属对象类型
	
	private Long kindId;//所属对象Id
	
	private String type;//分摊类型名称
	
	private Double amount;//成本数额
	
	private String month;//所属年月份
	
	private String fromExcel;
	

	public String getFromExcel() {
		return fromExcel;
	}

	public void setFromExcel(String fromExcel) {
		this.fromExcel = fromExcel;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getKindId() {
		return kindId;
	}

	public void setKindId(Long kindId) {
		this.kindId = kindId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	

}
