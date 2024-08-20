package com.cmwebgame.dao.portal;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cmwebgame.dao.IdEntity;
import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Performance;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.entities.vo.PerformanceV;
import com.cmwebgame.service.PerformanceManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class PerformanceDao extends InitDao<Performance> implements PerformanceManager {

	@Override
	public Performance save(
			Performance performance) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(performance);
		if(performance.getId() == null){
			return this.add(performance);
		}else {
			return this.update(performance);
		}
	}
	
	private Performance add(Performance performance){
		int id = this.insert(performance);
		if(id != 0){
			performance.setId(new Long(id));
			return performance;
		}else {
			return null;
		}
	}
	
	private Performance update(Performance performance){
		if(this.modify(performance)){
			return performance;
		}else {
			return null;
		}
	}

	@Override
	public PerformanceV getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from performance as pe join project as pr where pe.id=? and pe.project_id=pr.id";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		PerformanceV performanceV = null;
		if (list != null) {
			HashMap<String, Object> map = list.get(0);
			if (map != null) {
				performanceV = this.makePerformanceV(map);
			}
		}
		return performanceV;
	}

	@Override
	public List<PerformanceV> findByProjectId(
			Long projectId) {
		// TODO Auto-generated method stub
		String sql = "select * from performance` as pe join project as pr where pe.project_id=? and pe.project_id=pr.id order by pe.id desc";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(projectId);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<PerformanceV> performanceVs = Lists.newArrayList();
		if (list != null) {
			for(HashMap<String, Object> map : list){
				PerformanceV performanceV = this.makePerformanceV(map);
				performanceVs.add(performanceV);
			}
		}
			
		return performanceVs;
	}

	@Override
	public PerformanceV getByProjectIdAndMonth(
			Long projectId, String month) {
		// TODO Auto-generated method stub
		String sql = "select * from performance as pe join project as pr where pe.project_id=? and pe.`month`=? and pe.project_id=pr.id";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(projectId);
		conditions.add(month);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		return (list==null||list.size()==0)?null:makePerformanceV(list.get(0));
	}

	@Override
	public PageVo<PerformanceV> findPageVo(int pageNum,
			int rows) {
		// TODO Auto-generated method stub
		PageVo<PerformanceV> pageVo = new PageVo<PerformanceV>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows));
		pageVo.setCount(this.count());
		return pageVo;
	}

	@Override
	public List<PerformanceV> find(int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from `performance` as pe join project as pr where 1=1 and pe.project_id=pr.id order by pe.id desc limit ?,?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(offset);
		conditions.add(rows);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<PerformanceV> performanceVs = Lists.newArrayList();
		if (list != null) {
			for(HashMap<String, Object> map : list){
				PerformanceV performanceV = this.makePerformanceV(map);
				performanceVs.add(performanceV);
			}
		}
		return performanceVs;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from `performance` where 1=1";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	private PerformanceV makePerformanceV(HashMap<String, Object> map){
		PerformanceV p = new PerformanceV();
		p.setAmount((Double)map.get("performance.amount"));
		p.setCreateTime((Timestamp)map.get("performance.create_time"));
		p.setId((Long)map.get("performance.id"));
		p.setMonth((String)map.get("performance.month"));
		p.setProjectId((Long)map.get("performance.project_id"));
		p.setProjectName((String)map.get("project.name"));
		return p;
	}

	@Override
	public List<PerformanceV> findByCompanyIdAndMonth(Long companyId,
			String month) {
		// TODO Auto-generated method stub
		String sql = "select * from performance as p RIGHT join "
                + "(SELECT DISTINCT(pe.project_id) from project_employee as pe "
                + "right join (SELECT * from employee where company_id=?) as e on pe.employee_id=e.id) "
                + "as t on p.project_id=t.project_id left join project as pr on p.project_id=pr.id where p.month=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		conditions.add(month);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<PerformanceV> performanceVs = Lists.newArrayList();
		if (list != null) {
			for(HashMap<String, Object> map : list){
				PerformanceV performanceV = this.makePerformanceV(map);
				performanceVs.add(performanceV);
			}
		}
			
		return performanceVs;
	}

	@Override
	public PageVo<PerformanceV> findPageByConditions(
			HashMap<String, Object> conditions, int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<PerformanceV> pageVo = new PageVo<PerformanceV>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(conditions, offset, rows));
		pageVo.setCount(this.count(conditions));
		return pageVo;
	}
	
	public List<PerformanceV> find(HashMap<String, Object> conditionsMap, int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from `performance` as pe join project as pr where 1=1 and pe.project_id=pr.id";
		List<Object> conditions = Lists.newArrayList();
		if (conditionsMap!=null && conditionsMap.size()!=0) {
			Iterator<Entry<String, Object>> iterator = conditionsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				sql += " and pe."+key+"=?";
				conditions.add(value);
			}
		}
		sql += " order by pe.id desc limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<PerformanceV> performanceVs = Lists.newArrayList();
		if (list != null) {
			for(HashMap<String, Object> map : list){
				PerformanceV performanceV = this.makePerformanceV(map);
				performanceVs.add(performanceV);
			}
		}
		return performanceVs;
	}

	@Override
	public int count(HashMap<String, Object> conditionsMap) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from `performance` where 1=1";
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
