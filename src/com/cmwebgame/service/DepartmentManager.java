package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Department;
import com.cmwebgame.entities.vo.DepartmentV;
import com.cmwebgame.entities.vo.PageVo;

public interface DepartmentManager {
	
	public Department save(Department department);
	
	public DepartmentV getById(Long id);
	
	public PageVo<DepartmentV> findByPage(int pageNum , int rows, String orderBy, String orderName);
	
	public List<DepartmentV> find(int offset , int rows, String orderBy, String orderName);
	
	public int count();
	
	public List<Department> findByCompanyId(Long companyId);
	/**
	 * 
	 * @param companyId
	 * @param name
	 * @return true存在，false不存在
	 */
	public boolean isExist(Long companyId, String name);

}
