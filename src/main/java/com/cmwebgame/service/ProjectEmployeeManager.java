package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.ProjectEmployee;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.entities.vo.ProjectEmployeeV;

public interface ProjectEmployeeManager {
	
	public ProjectEmployee save(ProjectEmployee projectEmployee);
	
	public ProjectEmployeeV getById(Long id);
	/**
	 * 
	 * @param projectId （不能为空）
	 * @param employeeId （不能为空）
	 * @param month （不能为空）
	 * @return
	 */
	public ProjectEmployeeV getByProjectIdAndEmployeeId(Long projectId, Long employeeId, String month);
	/**
	 * 
	 * @param projectId 可为null，null时不作为查询条件
	 * @param employeeId 可为null，null时不作为查询条件
	 * @param month 可为null，null时不作为查询条件
	 * @param pageNum
	 * @param rows
	 * @return
	 */
	public PageVo<ProjectEmployeeV> findByPage(Long projectId, Long employeeId, String month, int pageNum, int rows);
	/**
	 * 
	 * @param projectId 可为null，null时不作为查询条件
	 * @param employeeId 可为null，null时不作为查询条件
	 * @param month 可为null，null时不作为查询条件
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<ProjectEmployeeV> find(Long projectId, Long employeeId, String month, int offset, int rows);
	/**
	 * 根据projectId或employeeId获取ProjectEmployeeV
	 * @param projectId 可为null，null时不作为查询条件
	 * @param employeeId 可为null，null时不作为查询条件
	 * @param month 可为null，null时不作为查询条件
	 * @return
	 */
	public List<ProjectEmployeeV> find(Long projectId, Long employeeId, String month);
	/**
	 * 
	 * @param projectId 可为null，null时不作为查询条件
	 * @param employeeId 可为null，null时不作为查询条件
	 * @param month 可为null，null时不作为查询条件
	 * @return
	 */
	public int count(Long projectId, Long employeeId, String month);
	
	public List<ProjectEmployeeV> findByMonth(String month);

}
