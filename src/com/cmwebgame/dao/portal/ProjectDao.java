package com.cmwebgame.dao.portal;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Project;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.ProjectManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class ProjectDao extends InitDao<Project> implements ProjectManager {

	@Override
	public Project save(Project project) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(project);
		if(project.getId() == null){
			return this.add(project);
		}else
			return this.update(project);
	}
	
	private Project add(Project project){
		int id = this.insert(project);
		if(id != 0){
			project.setId(new Long(id));
			return project;
		}else {
			return null;
		}
	}
	
	private Project update(Project project){
		if(this.modify(project)){
			return project;
		}else
			return null;
	}

	@Override
	public Project getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from project where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);		
		List<Project> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}
	
	@Override
	public Project getByName(String name) {
		// TODO Auto-generated method stub
		String sql = "select * from project where name=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(name);		
		List<Project> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public PageVo<Project> findByPage(int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<Project> pageVo = new PageVo<Project>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows));
		pageVo.setCount(this.count());
		return pageVo;
	}

	@Override
	public List<Project> find(int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from project order by id desc limit ?,?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(offset);		
		conditions.add(rows);		
		List<Project> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from project";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	@Override
	public int countByIsBusiness(String isBusiness) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from project where is_business=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(isBusiness);
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public List<Project> findByCompanyId(Long companyId) {
		// TODO Auto-generated method stub
		String sql = "select * from project as p RIGHT join "
                     + "(SELECT DISTINCT(pe.project_id) from project_employee as pe "
                     + "right join (SELECT * from employee where company_id=?) as e on pe.employee_id=e.id) "
                     + "as t on p.id=t.project_id";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);		
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<Project> projects = Lists.newArrayList();
		for (HashMap<String, Object> map : list) {
			Project project = this.makeProject(map);
			projects.add(project);
		}
		return projects;
	}
	
	private Project makeProject(HashMap<String, Object> map){
		Project project = new Project();
		project.setCreateTime((Timestamp)map.get("project.create_time"));
		project.setDescription((String)map.get("project.description"));
		project.setId((Long)map.get("project.id"));
		project.setIsBusiness((String)map.get("project.is_business"));
		project.setName((String)map.get("project.name"));
		project.setSpecialAlgorithm((String)map.get("project.special_algorithm"));
		return project;
	}

	@Override
	public PageVo<Project> findPageByConditions(
			HashMap<String, Object> conditions, int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<Project> pageVo = new PageVo<Project>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(conditions, offset, rows));
		pageVo.setCount(this.count(conditions));
		return pageVo;
	}
	
	@Override
	public List<Project> find(HashMap<String, Object> conditionsMap, int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from project where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if (conditionsMap!=null && conditionsMap.size()!=0) {
			Iterator<Entry<String, Object>> iterator = conditionsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				sql += " and `"+key+"`=?";
				conditions.add(value);
			}
		}
		sql += " order by id desc limit ?,?";
		conditions.add(offset);		
		conditions.add(rows);		
		List<Project> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int count(HashMap<String, Object> conditionsMap) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from project where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if (conditionsMap!=null && conditionsMap.size()!=0) {
			Iterator<Entry<String, Object>> iterator = conditionsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				sql += " and `"+key+"`=?";
				conditions.add(value);
			}
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

}
