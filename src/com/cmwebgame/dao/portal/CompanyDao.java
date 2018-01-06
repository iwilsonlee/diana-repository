package com.cmwebgame.dao.portal;

import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Company;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.CompanyManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class CompanyDao extends InitDao<Company> implements CompanyManager {

	@Override
	public Company save(Company company) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(company);
		if(company.getId() == null){
			return this.add(company);
		}else {
			return this.update(company);
		}
	}
	
	private Company add(Company company){
		int id = this.insert(company);
		if(id != 0){
			company.setId(new Long(id));
			return company;
		}else {
			return company;
		}
	}
	
	private Company update(Company company){
		if(this.modify(company)){
			return company;
		}else {
			return null;
		}
	}

	@Override
	public Company getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from company where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Company> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public PageVo<Company> findByPage(int pageNum, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		PageVo<Company> pageVo = new PageVo<Company>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows, orderBy, orderName));
		pageVo.setCount(this.count());
		
		return pageVo;
	}

	@Override
	public List<Company> find(int offset, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		String sql = "select * from company";
		List<Object> conditions = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(orderBy) && !Strings.isNullOrEmpty(orderName)) {
			sql += " order by " + orderName + " " +orderBy;
		}else {
			sql += " order by id desc";
		}
		sql += " limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<Company> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from company";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public Company getByName(String name) {
		// TODO Auto-generated method stub
		String sql = "select * from company where `name`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(name);
		List<Company> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public List<Long> findByAll() {
		// TODO Auto-generated method stub
		String sql = "select id from company";
		List<Object> conditions = Lists.newArrayList();
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class);
		return list;
	}

}
