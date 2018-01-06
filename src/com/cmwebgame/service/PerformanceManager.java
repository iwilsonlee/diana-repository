package com.cmwebgame.service;

import java.util.HashMap;
import java.util.List;

import com.cmwebgame.entities.Performance;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.entities.vo.PerformanceV;

public interface PerformanceManager {
	
	public Performance save(Performance performance);
	
	public PerformanceV getById(Long id);
	
	public List<PerformanceV> findByProjectId(Long projectId);
	
	public PerformanceV getByProjectIdAndMonth(Long projectId, String month);
	
	public List<PerformanceV> findByCompanyIdAndMonth(Long companyId, String month);
	
	public PageVo<PerformanceV> findPageVo(int pageNum, int rows);
	/**
	 * 根据查询条件获取业绩分页，查询条件是hashmap，key是sql字段名，value是字段值
	 * @param conditions
	 * @param pageNum
	 * @param rows
	 * @return
	 */
	public PageVo<PerformanceV> findPageByConditions(HashMap<String, Object> conditions, int pageNum, int rows);
	
	public List<PerformanceV> find(int offset, int rows);
	
	public int count();
	/**
	 * 根据查询条件获取业绩数量，查询条件是hashmap，key是sql字段名，value是字段值
	 * @param conditions
	 * @return
	 */
	public int count(HashMap<String, Object> conditions);

}
