package com.cmwebgame.dao.portal;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Position;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.entities.vo.PositionV;
import com.cmwebgame.service.PositionManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class PositionDao extends InitDao<Position> implements PositionManager {

	@Override
	public Position save(Position position) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(position);
		if(position.getId() == null){
			return this.add(position);
		}else {
			return this.update(position);
		}
	}
	
	private Position add(Position position){
		int id = this.insert(position);
		if(id != 0){
			position.setId(new Long(id));
			return position;
		}else
			return null;
	}
	
	private Position update(Position position){
		if(this.modify(position)){
			return position;
		}else {
			return null;
		}
	}

	@Override
	public Position getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from `position` where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Position> list = this.getResultSetByCondition(sql, conditions);
		
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public PageVo<Position> findByPage(int pageNum, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		PageVo<Position> pageVo = new PageVo<Position>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows, orderBy, orderName));
		pageVo.setCount(this.count());
		return pageVo;
	}

	@Override
	public List<Position> find(int offset, int rows, String orderBy, String orderName) {
		// TODO Auto-generated method stub
		String sql = "select * from `position`";
		List<Object> conditions = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(orderBy) && !Strings.isNullOrEmpty(orderName)) {
			sql += " order by " + orderName + " " +orderBy;
		}else {
			sql += " order by id desc";
		}
		sql += " limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<Position> list = this.getResultSetByCondition(sql, conditions);
//		List<PositionV> positionVs = Lists.newArrayList();
//		for(HashMap<String, Object> map : list){
//			PositionV positionV = makepPositionV(map);
//			positionVs.add(positionV);
//		}
		
		return list;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from position";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	private PositionV makepPositionV(HashMap<String, Object> map){
		PositionV p = new PositionV();
		p.setCreateTime((Timestamp)map.get("position.create_time"));
		p.setDescription((String)map.get("position.description"));
		p.setId((Long)map.get("position.id"));
		p.setName((String)map.get("position.name"));
		return p;
	}

	@Override
	public List<Position> findAll() {
		// TODO Auto-generated method stub
		String sql = "select * from `position`";
		List<Object> conditions = Lists.newArrayList();
		List<Position> list = this.getResultSetByCondition(sql, conditions);
		
		return list;
	}

}
