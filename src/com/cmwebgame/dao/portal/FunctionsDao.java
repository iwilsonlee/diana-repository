package com.cmwebgame.dao.portal;

import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Functions;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.FunctionsManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class FunctionsDao extends InitDao<Functions> implements
		FunctionsManager {

	@Override
	public Functions save(Functions functions) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(functions);
		if(functions.getId() == null){
			return this.add(functions);
		}else {
			return this.update(functions);
		}
	}
	
	private Functions add(Functions functions){
		int id = this.insert(functions);
		if(id != 0){
			functions.setId(new Long(id));
			return functions;
		}else {
			return null;
		}
	}
	
	private Functions update(Functions functions){
		if(this.modify(functions)){
			return functions;
		}else {
			return null;
		}
	}

	@Override
	public Functions getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from functions where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Functions> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public PageVo<Functions> findByPage(int pageNum, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		PageVo<Functions> pageVo = new PageVo<Functions>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows, orderBy, orderName));
		pageVo.setCount(this.count());
		
		return pageVo;
	}

	@Override
	public List<Functions> find(int offset, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		String sql = "select * from `functions`";
		List<Object> conditions = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(orderBy) && !Strings.isNullOrEmpty(orderName)) {
			sql += " order by " + orderName + " " +orderBy;
		}else {
			sql += " order by id desc";
		}
		sql += " limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<Functions> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from `functions`";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

}
