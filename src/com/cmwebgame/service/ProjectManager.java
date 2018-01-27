package com.cmwebgame.service;

import java.util.HashMap;
import java.util.List;

import com.cmwebgame.entities.Project;
import com.cmwebgame.entities.vo.PageVo;

public interface ProjectManager {
	
	public Project save(Project project);
	
	public Project getById(Long id);
	
	public Project getByName(String name);
	
	public PageVo<Project> findByPage(int pageNum, int rows);
	/**
	 * 根据查询条件获取项目分页，查询条件是hashmap，key是sql字段名，value是字段值
	 * @param conditions
	 * @param pageNum
	 * @param rows
	 * @return
	 */
	public PageVo<Project> findPageByConditions(HashMap<String, Object> conditions, int pageNum, int rows);
	
	public List<Project> find(int offset, int rows);
	
	public List<Project> find(HashMap<String, Object> conditionsMap, int offset, int rows);
	
	public int count();
	/**
	 * 根据查询条件获取数量，查询条件是hashmap，key是sql字段名，value是字段值
	 * @param conditions
	 * @return
	 */
	public int count(HashMap<String, Object> conditions);
	
	public int countByIsBusiness(String isBusiness);
	
	public List<Project> findByCompanyId(Long companyId);

}
