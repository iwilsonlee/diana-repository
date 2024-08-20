package com.cmwebgame.dao.portal;

import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.ModuleContent;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.ModuleContentManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class ModuleContentDao extends InitDao<ModuleContent> implements
		ModuleContentManager {

	@Override
	public ModuleContent save(ModuleContent moduleContent) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(moduleContent);
		if(moduleContent.getId() == null){
			return this.add(moduleContent);
		}else {
			return this.update(moduleContent);
		}
	}
	
	private ModuleContent add(ModuleContent moduleContent){
		int id = this.insert(moduleContent);
		if (id != 0) {
			moduleContent.setId(new Long(id));
			return moduleContent;
		}
		return null;
	}
	
	private ModuleContent update(ModuleContent moduleContent){
		if(this.modify(moduleContent)){
			return moduleContent;
		}
		return null;
	}

	@Override
	public ModuleContent getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from module_content where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<ModuleContent> moduleContents = this.getResultSetByCondition(sql, conditions);
		return (moduleContents==null||moduleContents.size()==0)?null:moduleContents.get(0);
	}

	@Override
	public List<Long> findAllByModuleId(Long moduleId) {
		// TODO Auto-generated method stub
		String sql = "select id from module_content where module_id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(moduleId);
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class);
		return list;
	}

	@Override
	public PageVo<ModuleContent> findPageByModuleId(Long moduleId, int pageNum,
			int rows) {
		// TODO Auto-generated method stub
		PageVo<ModuleContent> pageVo = new PageVo<ModuleContent>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.findByModuleId(moduleId, offset, rows));
		pageVo.setCount(this.count(moduleId));
		
		
		return pageVo;
	}
	
	public List<ModuleContent> findByModuleId(Long moduleId, int pageNum,
			int rows){
		String sql = "select * from module_content where module_id=? limit ?,?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(moduleId);
		conditions.add(pageNum);
		conditions.add(rows);
		List<ModuleContent> moduleContents = this.getResultSetByCondition(sql, conditions);
		return moduleContents;
	}

	@Override
	public int count(Long moduleId) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from module_content where module_id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(moduleId);
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public List<Long> findAllByUrl(String url) {
		// TODO Auto-generated method stub
		String sql = "select id from module_content where `url`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(url);
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class);
		return list;
	}

}
