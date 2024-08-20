package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.ModuleContent;
import com.cmwebgame.entities.vo.PageVo;

public interface ModuleContentManager {
	
	public ModuleContent save(ModuleContent moduleContent);
	
	public ModuleContent getById(Long id);
	
	public List<Long> findAllByModuleId(Long moduleId);
	
	public List<Long> findAllByUrl(String url);
	
	public PageVo<ModuleContent> findPageByModuleId(Long moduleId, int pageNum, int rows);
	
	public int count(Long moduleId);

}
