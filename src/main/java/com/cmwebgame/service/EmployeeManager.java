package com.cmwebgame.service;

import java.util.HashMap;
import java.util.List;

import com.cmwebgame.entities.Employee;
import com.cmwebgame.entities.vo.EmployeeV;
import com.cmwebgame.entities.vo.PageVo;

public interface EmployeeManager {
	
	public Employee save(Employee employee);
	
	public Employee getById(Long id);
	
	public Employee getByJobNumber(String jobNumber);
	
	public EmployeeV getEmployeeVById(Long id);
	
	public Employee getByEmail(String email);
	
	public PageVo<EmployeeV> findPage(int pageNum, int rows);
	/**
	 * 根据查询条件获取人员分页，查询条件是hashmap，key是sql字段名，value是字段值
	 * @param conditions 查询条件
	 * @param pageNum 页码
	 * @param rows 查询行数
	 * @param orderBy 排序，desc或asc
	 * @param orderName 排序依据字段名
	 * @return
	 */
	public PageVo<EmployeeV> findPageByConditions(HashMap<String, Object> conditions,int pageNum, int rows, String orderBy, String orderName, boolean isAllStatus);
	/**
	 * 根据查询条件获取人员数量，查询条件是hashmap，key是sql字段名，value是字段值
	 * @param conditions
	 * @return
	 */
	public int count(HashMap<String, Object> conditions, boolean isAllStatus);
	
	public List<EmployeeV> find(int offset, int rows);
	
	public int count();
	
	public int countByIsFulltime(String isFullTime);
	/**
	 * 
	 * @param companyId
	 * @param employeeStatus 为null时不作为搜索条件
	 * @return
	 */
	public int countByCompanyId(Long companyId, String employeeStatus);
	/**
	 * 获取指定公司指定月份的正职人数(不包含非正职，但包含当月离职的正职人员)
	 * @param companyId
	 * @param month
	 * @return
	 */
	public int countByCompanyIdAndMonth(Long companyId, String month);
	
	public List<Employee> findLikeName(String name);
	
	public List<Employee> findByName(String name);
	/**
	 * 
	 * @param companyId
	 * @param employeeStatus 为null时不作为搜索条件
	 * @return
	 */
	public List<Employee> findByCompanyId(Long companyId, String employeeStatus);
	/**
	 * 获取指定公司指定月份的正职人员列表(不包含非正职，但包含当月离职的正职人员)
	 * @param companyId
	 * @param month
	 * @return
	 */
	public List<Employee> findByCompanyIdAndMonth(Long companyId, String month);
	/**
	 * 获取指定月份的所有正职人员列表(不包含非正职，但包含当月离职的正职人员)
	 * @param companyId
	 * @param month
	 * @return
	 */
	public List<Employee> findByMonth(String month);
	
	public List<Long> findByAll();
}
