package com.cmwebgame.dao.portal;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.cmwebgame.constant.ProjectConstant;
import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Apportionment;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.ApportionmentManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ApportionmentDao extends InitDao<Apportionment> implements
		ApportionmentManager {

	@Override
	public Apportionment save(Apportionment apportionment) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(apportionment);
		if (apportionment.getId() != null) {
			return update(apportionment);
		}else {
			return add(apportionment);
		}
	}
	
	public Apportionment add(Apportionment apportionment){
		int id = this.insert(apportionment);
		if (id != 0) {
			apportionment.setId(new Long(id));
			return apportionment;
		}
		return null;
	}
	public Apportionment update(Apportionment apportionment){
		if (this.modify(apportionment)) {
			return apportionment;
		}
		return null;
	}

	@Override
	public Apportionment getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from apportionment where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Apportionment> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public List<Apportionment> findByMonth(String month) {
		// TODO Auto-generated method stub
		String sql = "select * from apportionment where `month`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(month);
		List<Apportionment> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public PageVo<Apportionment> findPageByMonth(String month, int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<Apportionment> pageVo = new PageVo<Apportionment>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.findByMonth(month, offset, rows));
		pageVo.setCount(this.count(month));
		
		return pageVo;
	}
	
	public List<Apportionment> findByMonth(String month, int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from apportionment";
		List<Object> conditions = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(month)) {
			sql += " where `month`=?";
			conditions.add(month);
		}
		sql += " limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<Apportionment> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public int count(String month) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from apportionment";
		List<Object> conditions = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(month)) {
			sql += " where `month`=?";
			conditions.add(month);
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public Apportionment getByProjectIdAndNameAndMonth(Long projectId, Long companyId,
			String name, String month) {
		// TODO Auto-generated method stub
		String sql = "select * from apportionment where project_id=? and company_id=? and `name`=? and `month`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(projectId);
		conditions.add(companyId);
		conditions.add(name);
		conditions.add(month);
		List<Apportionment> list = this.getResultSetByCondition(sql, conditions);
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public LinkedHashMap<String, List<Apportionment>> findMapByMonth(Long companyId, String month) {
		// TODO Auto-generated method stub
		
		return findMapByMonthAndProjectStatus(companyId, month, ProjectConstant.Status.on.name());
	}
	
	public LinkedHashMap<String, List<Apportionment>> findMapByMonthAndProjectStatus(Long companyId, String month, String pojectStatus) {
		// TODO Auto-generated method stub
		List<Object> conditions = Lists.newArrayList();
		String sql = "select DISTINCT(project_id) from apportionment where `month`=?";
		if (!Strings.isNullOrEmpty(pojectStatus)) {
			sql = "select DISTINCT(apportionment.project_id) from apportionment " +
					"join project on project.`status`=? and apportionment.project_id=project.id " +
					"where apportionment.`month`=?";
			conditions.add(pojectStatus);
		}
		
		conditions.add(month);
		List<Long> projectIdList = this.getResultSetByCondition(sql, conditions, Long.class);
		LinkedHashMap<String, List<Apportionment>> map = Maps.newLinkedHashMap();
//		double total = 0.0;
		for (Long projectId : projectIdList) {
			List<Apportionment> apportionments = findByProjectIdAndMonth(projectId, companyId, month);
			if (apportionments!=null&&apportionments.size()!=0) {
				map.put(apportionments.get(0).getProjectName(), apportionments);
//				for(Apportionment a : apportionments){
//					if(a.getProjectName().equals("业务-唯品国际牛尔")){
//						System.out.println("==projectId=" + projectId + " | projectName=" + a.getProjectName() + 
//								" | " + a.getName() + "=" + 
//								a.getAmount());
//					}
//					
//				}
			}
			
		}
//		System.out.println("========================");
//		System.out.println("The companyId=" + companyId + " | total=" + total + " | size=" + map.size());
		return map;
	}

	@Override
	public List<Apportionment> findByProjectIdAndMonth(Long projectId, Long companyId, String month) {
		// TODO Auto-generated method stub
		String sql = "select * from apportionment where project_id=? and company_id=? and month=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(projectId);
		conditions.add(companyId);
		conditions.add(month);
		List<Apportionment> list = this.getResultSetByCondition(sql, conditions);
		return list;
	}

	@Override
	public List<String> findNameByMonth(Long companyId, String month) {
		// TODO Auto-generated method stub
		String sql = "select DISTINCT(`name`) from apportionment where company_id=? and month=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		conditions.add(month);
		List<String> list = this.getResultSetByCondition(sql, conditions, String.class);
		return list;
	}

	@Override
	public boolean clearAmountByMonth(String month, String fromExcel) {
		// TODO Auto-generated method stub
		String sql = "update apportionment set amount=0.0 where `month`=? and from_excel=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(month);
		conditions.add(fromExcel);
		return this.modify(sql, conditions);
	}

}
