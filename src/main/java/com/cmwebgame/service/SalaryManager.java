package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Salary;
import com.cmwebgame.entities.vo.PageVo;

public interface SalaryManager {
	
	public Salary save(Salary salary);
	
	
	public Salary getById(Long id);
	
	public Salary getByEmployeeIdAndMonth(Long employeeId, String month);
	
	public PageVo<Salary> findPage(Long employeeId, int pageNum, int rows);
	
	public List<Salary> find(Long employeeId, int offset, int rows);
	
	public int count(Long employeeId);
	
	/**
	 * 获取指定月份的薪资列表
	 * @param month
	 * @return
	 */
	public List<Salary> findByMonth(String month);
	
}
