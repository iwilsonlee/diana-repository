package com.cmwebgame.dao.portal;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Department;
import com.cmwebgame.entities.vo.DepartmentV;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.DepartmentManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class DepartmentDao extends InitDao<Department> implements
		DepartmentManager {

	@Override
	public Department save(Department department) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(department);
		if(department.getId() == null){
			return this.add(department);
		}else {
			return this.update(department);
		}
	}
	
	private Department add(Department department){
		int id = this.insert(department);
		if(id != 0){
			department.setId(new Long(id));
			return department;
		}else {
			return null;
		}
	}
	
	private Department update(Department department){
		if(this.modify(department)){
			return department;
		}else
			return null;
	}

	@Override
	public DepartmentV getById(Long id) {
		// TODO Auto-generated method stub
		String sql ="select * from department as d join company as c where d.id=? and d.company_id=c.id";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		HashMap<String, Object> map = (list==null||list.size()==0)?null:list.get(0);
		DepartmentV departmentV = null;
		if (map != null) {
			departmentV = this.makeDepartmentV(map);
		}
		return departmentV;
	}

	@Override
	public PageVo<DepartmentV> findByPage(int pageNum, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		PageVo<DepartmentV> pageVo = new PageVo<DepartmentV>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows, orderBy, orderName));
		pageVo.setCount(this.count());
		
		return pageVo;
	}

	@Override
	public List<DepartmentV> find(int offset, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		String sql ="select * from department as d join company as c where d.company_id=c.id";
		List<Object> conditions = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(orderBy) && !Strings.isNullOrEmpty(orderName)) {
			sql += " order by d." + orderName + " " +orderBy;
		}else {
			sql += " order by d.id desc";
		}
		sql += " limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<DepartmentV> departmentVs = Lists.newArrayList();
		for (HashMap<String, Object> map : list) {
			DepartmentV departmentV = this.makeDepartmentV(map);
			departmentVs.add(departmentV);
		}
		return departmentVs;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		String sql ="select count(*) from department";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	private DepartmentV makeDepartmentV(HashMap<String, Object> map){
		DepartmentV departmentV = new DepartmentV();
		departmentV.setCompanyId((Long)map.get("department.company_id"));
		departmentV.setCompanyName((String)map.get("company.name"));
		departmentV.setCreateTime((Timestamp)map.get("department.create_time"));
		departmentV.setDescription((String)map.get("department.description"));
		departmentV.setId((Long)map.get("department.id"));
		departmentV.setName((String)map.get("department.name"));
		departmentV.setStatus((String)map.get("department.status"));
		return departmentV;
	}

	@Override
	public List<Department> findByCompanyId(Long companyId) {
		// TODO Auto-generated method stub
		String sql ="select * from department where company_id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		List<Department> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public boolean isExist(Long companyId, String name) {
		// TODO Auto-generated method stub
		String sql ="select count(*) from department where company_id=? and `name`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		conditions.add(name);
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0||list.get(0)==0)?false:true;
	}

}
