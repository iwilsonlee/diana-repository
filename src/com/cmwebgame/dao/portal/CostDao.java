package com.cmwebgame.dao.portal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Cost;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.CostManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class CostDao extends InitDao<Cost> implements CostManager {

	@Override
	public Cost save(Cost cost) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(cost);
		if(cost.getId() == null){
			return add(cost);
		}else {
			return update(cost);
		}
	}
	
	private Cost add(Cost cost){
		int id = this.insert(cost);
		if(id != 0){
			cost.setId(new Long(id));
			return cost;
		}
		return null;
	}
	
	private Cost update(Cost cost){
		if(this.modify(cost)){
			return cost;
		}
		return null;
	}

	@Override
	public Cost getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from cost where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Cost> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public List<Cost> findByMonth(String month) {
		// TODO Auto-generated method stub
		String sql = "select * from cost where `month`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(month);
		List<Cost> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public PageVo<Cost> findPage(int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<Cost> pageVo = new PageVo<Cost>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows));
		pageVo.setCount(this.count());
		
		return pageVo;
	}
	
	public List<Cost> find(int offset, int rows){
		String sql = "select * from cost limit ?,?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(offset);
		conditions.add(rows);
		List<Cost> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from cost";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public Cost getByKindAndKindIdAndNameAndMonth(String kind, Long kindId, String name, String month) {
		// TODO Auto-generated method stub
		String sql = "select id from cost where kind=? and kind_id=? and `name`=? and `month`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(kind);
		conditions.add(kindId);
		conditions.add(name);
		conditions.add(month);
		List<Cost> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public boolean saveOnTransaction(List<Cost> costs) {
		// TODO Auto-generated method stub
		Connection connection = this.getConnection();
		boolean result = false;
		try {
			connection.setAutoCommit(false);
			for (Cost cost: costs) {
				if (cost.getId()==null) {
					int newId = this.insert(connection, cost);
				}else {
					this.modify(connection, cost);
				}
			}
			connection.commit();
//			connection.setAutoCommit(true);//回复事务默认提交方式
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean clearAmountByMonth(String month, String fromExcel) {
		// TODO Auto-generated method stub
		String sql = "update cost set amount=0.0 where `month`=? and from_excel=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(month);
		conditions.add(fromExcel);
		return this.modify(sql, conditions);
	}
	
}
