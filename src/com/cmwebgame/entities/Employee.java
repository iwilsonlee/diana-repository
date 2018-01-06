package com.cmwebgame.entities;

import java.sql.Timestamp;
import java.util.Date;

import com.cmwebgame.dao.IdEntity;

public class Employee extends IdEntity {
	
	private String name;
	
	private String ename;
	
	private String email;
	
	private String password;
	
	private String jobNumber;//工号
	
	private Date  birthday;
	
	private String gender;
	
	private Long departmentId;
	
	private Long positionId;
	
	private Long functionsId;
	/**
	 * 职称说明，默认为positionName+functionsName
	 */
	private String positionMemo;
	
	private Long companyId;
	
	private String isFulltime;
	
	private String status;
	
	private Timestamp createTime;
	
	private Timestamp lastLoginTime;
	/**
	 * 入职时间
	 */
	private String entryDate;
	/**
	 * 离职日期
	 */
	private String leaveJobDate;
	

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getLeaveJobDate() {
		return leaveJobDate;
	}

	public void setLeaveJobDate(String leaveJobDate) {
		this.leaveJobDate = leaveJobDate;
	}

	public String getPositionMemo() {
		return positionMemo;
	}

	public void setPositionMemo(String positionMemo) {
		this.positionMemo = positionMemo;
	}

	public Long getFunctionsId() {
		return functionsId;
	}

	public void setFunctionsId(Long functionsId) {
		this.functionsId = functionsId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getIsFulltime() {
		return isFulltime;
	}

	public void setIsFulltime(String isFulltime) {
		this.isFulltime = isFulltime;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	

}
