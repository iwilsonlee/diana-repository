package com.cmwebgame.dao.portal;

import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Message;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.MessageManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class MessageDao extends InitDao<Message> implements MessageManager {

	@Override
	public Message save(Message message) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(message);
		if(message.getId() == null){
			return add(message);
		}else
			return update(message);
	}
	
	private Message add(Message message){
		int id = this.insert(message);
		if(id!=0){
			message.setId(new Long(id));
			return message;
		}
		return null;
	}
	private Message update(Message message){
		if(this.modify(message)){
			return message;
		}
		return null;
	}

	@Override
	public Message getById(Long id) {
		// TODO Auto-generated method stub
		String sql ="select * from `message` where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Message> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}
	
	@Override
	public Message getByMonth(String month) {
		// TODO Auto-generated method stub
		String sql ="select * from `message` where kind=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(month);
		List<Message> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public PageVo<Message> findPageByKindId(Long employeeId, int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<Message> pageVo = new PageVo<Message>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.findListByKindId(employeeId, offset, rows));
		pageVo.setCount(this.countByKindId(employeeId));
		
		return pageVo;
	}
	
	public List<Message> findListByKindId(Long employeeId, int offset, int rows){
		String sql ="select * from `message` where kind!='employee'";
		List<Object> conditions = Lists.newArrayList();
		if(employeeId!=null){
			sql += " or (kind='employee' and kind_id=?)";
			conditions.add(employeeId);
		}
		sql += " order by id desc limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<Message> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int countByKindId(Long employeeId) {
		// TODO Auto-generated method stub
		String sql ="select count(*) from `message` where kind!='employee'";
		List<Object> conditions = Lists.newArrayList();
		if(employeeId!=null){
			sql += " or (kind='employee' and kind_id=?)";
			conditions.add(employeeId);
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public int countNotReadByKindId(Long employeeId) {
		// TODO Auto-generated method stub
		String sql ="select count(*) from `message` where kind!='employee' and is_readed='no'";
		List<Object> conditions = Lists.newArrayList();
		if(employeeId!=null){
			sql += " or (kind='employee' and kind_id=?)";
			conditions.add(employeeId);
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

}
