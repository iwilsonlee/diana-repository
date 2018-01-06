package com.cmwebgame.dao.portal;

import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Module;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.ModuleManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class ModuleDao extends InitDao<Module> implements ModuleManager {

	@Override
	public Module save(Module module) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(module);
		if (module.getId() != null) {
			return update(module);
		}else {
			return add(module);
		}
	}
	
	private Module add(Module module){
		int id = this.insert(module);
		if (id != 0) {
			module.setId(new Long(id));
			return module;
		}
		return null;
	}
	
	private Module update(Module module){
		if (this.modify(module)) {
			return module;
		}
		return null;
	}

	@Override
	public Module getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from module where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Module> list = this.getResultSetByCondition(sql, conditions); 
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public List<Long> findByFatherModuleId(Long fatherModuleId) {
		// TODO Auto-generated method stub
		String sql = "select * from module where";
		List<Object> conditions = Lists.newArrayList();
		if (fatherModuleId == null) {
			sql += " father_module_id is null";
		}else {
			sql += " father_module_id=?";
			conditions.add(fatherModuleId);
		}
		sql += " order by `sort` asc";
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class); 
		return list;
	}

	@Override
	public PageVo<Module> findPageByFatherModuleId(Long fatherModuleId,
			int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<Module> pageVo = new PageVo<Module>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.findByFatherModuleId(fatherModuleId, offset, rows));
		pageVo.setCount(this.count(fatherModuleId));
		
		return pageVo;
	}
	
	public List<Module> findByFatherModuleId(Long fatherModuleId,
			int offset, int rows) {
		String sql = "select * from module where";
		List<Object> conditions = Lists.newArrayList();
		if (fatherModuleId==null) {
			sql += " father_module_id is null";
		}else {
			sql += " father_module_id=?";
			conditions.add(fatherModuleId);
		}
		
		sql += " order by `sort` asc limit ?,?";
		
		conditions.add(offset);
		conditions.add(rows);
		List<Module> list = this.getResultSetByCondition(sql, conditions); 
		return list;
	}

	@Override
	public int count(Long fatherModuleId) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from module where ";
		List<Object> conditions = Lists.newArrayList();
		if (fatherModuleId==null) {
			sql += "father_module_id is null";
		}else {
			sql += "father_module_id=?";
			conditions.add(fatherModuleId);
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class); 
		return (list==null||list.size()==0)?0:list.get(0);
	}
	

	@Override
	public List<Long> findByAll() {
		// TODO Auto-generated method stub
		String sql = "select id from module order by `sort`";
		List<Object> conditions = Lists.newArrayList();
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class); 
		return list;
	}

	@Override
	public Module getByURL(String uri) {
		// TODO Auto-generated method stub
		String sql = "select * from module where `url`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(uri);
		List<Module> list = this.getResultSetByCondition(sql, conditions); 
		return (list==null||list.size()==0)?null:list.get(0);
	}

}
