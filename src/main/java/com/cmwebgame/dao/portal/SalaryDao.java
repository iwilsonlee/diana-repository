package com.cmwebgame.dao.portal;

import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Salary;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.SalaryManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class SalaryDao extends InitDao<Salary> implements SalaryManager {

	@Override
	public Salary save(Salary salary) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(salary);
		if (salary.getId() == null) {
			return add(salary);
		}else {
			return update(salary);
		}
	}
	
	private Salary add(Salary salary){
		int id = this.insert(salary);
		if (id==0) {
			return null;
		}else {
			salary.setId(new Long(id));
			return salary;
		}
	}
	
	private Salary update(Salary salary){
		if (this.modify(salary)) {
			return salary;
		}else {
			return null;
		}
	}

	@Override
	public Salary getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from salary where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Salary> list = this.getResultSetByCondition(sql, conditions);
		if (list==null||list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public Salary getByEmployeeIdAndMonth(Long employeeId, String month) {
		// TODO Auto-generated method stub
		String sql = "select * from salary where employee_id=? and month=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(employeeId);
		conditions.add(month);
		List<Salary> list = this.getResultSetByCondition(sql, conditions);
		if (list==null||list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public PageVo<Salary> findPage(Long employeeId, int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<Salary> pageVo = new PageVo<Salary>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find( employeeId, offset, rows));
		pageVo.setCount(this.count( employeeId));
		return pageVo;
	}

	@Override
	public List<Salary> find(Long employeeId, int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from salary where employee_id=? order by `month` desc limit ?,?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(employeeId);
		conditions.add(offset);
		conditions.add(rows);
		List<Salary> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int count(Long employeeId) {
		// TODO Auto-generated method stub
		String sql = "select * from salary where employee_id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(employeeId);
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public List<Salary> findByMonth(String month) {
		// TODO Auto-generated method stub
		String sql = "select * from salary where `month`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(month);
		List<Salary> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

}
