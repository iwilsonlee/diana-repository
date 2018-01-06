package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Module;
import com.cmwebgame.entities.vo.ModuleV;
import com.cmwebgame.entities.vo.PageVo;

public interface ModuleManager {
	
	public Module save(Module module);
	
	public Module getById(Long id);
	
	public List<Long> findByFatherModuleId(Long fatherModuleId);
	
	public List<Long> findByAll();
	
	public Module getByURL(String uri);
	
	/**
	 * 获取模块分页
	 * @param fatherModuleId
	 * @param pageNum
	 * @param rows
	 * @return
	 */
	public PageVo<Module> findPageByFatherModuleId(Long fatherModuleId, int pageNum, int rows);
	
	/**
	 * 获取模块数量
	 * @param fatherModuleId
	 * @return
	 */
	public int count(Long fatherModuleId);

}
