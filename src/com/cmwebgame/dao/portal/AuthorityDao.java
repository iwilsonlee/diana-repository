package com.cmwebgame.dao.portal;

import java.util.HashMap;
import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Authority;
import com.cmwebgame.entities.vo.AuthorityV;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.AuthorityManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class AuthorityDao extends InitDao<Authority> implements AuthorityManager {

	@Override
	public Authority save(Authority authority) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(authority);
		if(authority.getId() == null){
			return this.add(authority);
		}else {
			return this.update(authority);
		}
	}
	
	private Authority add(Authority authority){
		int id = this.insert(authority);
		if(id != 0){
			authority.setId(new Long(id));
			return authority;
		}else {
			return null;
		}
	}
	
	private Authority update(Authority authority){
		if(this.modify(authority)){
			return authority;
		}else
			return null;
	}

	@Override
	public Authority getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from authority where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Authority> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
		
	}
	
	public AuthorityV getAuthorityVById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from authority as a left join employee as e on e.id=a.employee_id "
				+ "left join module as m on m.id=a.module_id where a.id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		AuthorityV authorityV = null;
		if (list != null && list.size()>0) {
			authorityV = this.makeAuthorityV(list.get(0));
		}
		return authorityV;
		
	}

	@Override
	public PageVo<AuthorityV> findByPage(Long employeeId, int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<AuthorityV> pageVo = new PageVo<AuthorityV>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(employeeId, offset, rows));
		pageVo.setCount(this.count(employeeId));
		
		
		return pageVo;
	}

	@Override
	public List<AuthorityV> find(Long employeeId, int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from authority as a left join employee as e on e.id=a.employee_id "
				+ "left join module as m on m.id=a.module_id where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if(employeeId != null){
			sql += " and a.employee_id=?";
			conditions.add(employeeId);
		}
		sql += " order by a.id desc limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<AuthorityV> authorityVs = Lists.newArrayList();
		for (HashMap<String, Object> map : list) {
			AuthorityV authorityV = this.makeAuthorityV(map);
			authorityVs.add(authorityV);
		}
		return authorityVs;
	}

	@Override
	public int count(Long employeeId) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from authority where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if(employeeId != null){
			sql += " and employee_id=?";
			conditions.add(employeeId);
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public List<Long> findByEmployeeId(Long employeeId) {
		// TODO Auto-generated method stub
		String sql = "select * from authority as a left join employee as e on e.id=a.employee_id "
				+ "left join module as m on m.id=a.module_id where a.employee_id=? order by m.sort asc";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(employeeId);
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class);
		return list;
	}
	
	private AuthorityV makeAuthorityV(HashMap<String, Object> map){
		AuthorityV authorityV = new AuthorityV();
		authorityV.setId((Long)map.get("authority.id"));
		authorityV.setModuleId((Long)map.get("authority.module_id"));
		authorityV.setModuleName((String)map.get("module.name"));
		authorityV.setModuleUrl((String)map.get("module.url"));
		authorityV.setPermission((String)map.get("authority.permission"));
		authorityV.setEmployeeId((Long)map.get("authority.employee_id"));
		authorityV.setPositionMemo((String)map.get("employee.positionMemo"));
		authorityV.setEmployeeName((String)map.get("employee.name"));
		authorityV.setDescription((String)map.get("authority.description"));
		authorityV.setModuleIcon((String)map.get("module.icon"));
		authorityV.setFatherModuleId((Long)map.get("module.father_module_id"));
		return authorityV;
	}

	@Override
	public List<Long> findByEmployeeIdAndFatherModuleId(Long employeeId,
			Long fatherModuleId) {
		// TODO Auto-generated method stub
		String sql = "select * from authority as a left join employee as e on e.id=a.employee_id "
				+ "left join module as m on m.id=a.module_id where a.employee_id=? and m.father_module_id=? order by m.sort asc";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(employeeId);
		conditions.add(fatherModuleId);
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class);
		
		return list;
	}

}
