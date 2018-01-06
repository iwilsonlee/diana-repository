package com.cmwebgame.dao.portal;

import java.util.HashMap;
import java.util.List;

import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.ProjectEmployee;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.entities.vo.PerformanceV;
import com.cmwebgame.entities.vo.ProjectEmployeeV;
import com.cmwebgame.service.ProjectEmployeeManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class ProjectEmployeeDao extends InitDao<ProjectEmployee> implements
		ProjectEmployeeManager {

	@Override
	public ProjectEmployee save(ProjectEmployee projectEmployee) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(projectEmployee);
		if(projectEmployee.getId() == null){
			return this.add(projectEmployee);
		}else
			return this.update(projectEmployee);
	}
	
	private ProjectEmployee add(ProjectEmployee projectEmployee){
		int id = this.insert(projectEmployee);
		if(id != 0){
			projectEmployee.setId(new Long(id));
			return projectEmployee;
		}else {
			return null;
		}
	}
	
	private ProjectEmployee update(ProjectEmployee projectEmployee){
		if(this.modify(projectEmployee)){
			return projectEmployee;
		}else
			return null;
	}

	@Override
	public ProjectEmployeeV getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from project_employee as pe left join project as pr on pe.project_id=pr.id "
				+ "left join employee as e on pe.employee_id=e.id "
				+ "where pe.id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		ProjectEmployeeV projectEmployeeV = null;
		if (list != null) {
			projectEmployeeV = this.makeProjectEmployeeV(list.get(0));
		}
		return projectEmployeeV;
	}

	@Override
	public PageVo<ProjectEmployeeV> findByPage(Long projectId, Long employeeId, String month,
			int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<ProjectEmployeeV> pageVo = new PageVo<ProjectEmployeeV>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(projectId, employeeId, month, offset, rows));
		pageVo.setCount(this.count(projectId, employeeId, month));
		return pageVo;
	}

	@Override
	public List<ProjectEmployeeV> find(Long projectId, Long employeeId, String month,
			int offset, int rows) {
		// TODO Auto-generated method stub
		String sql = "select * from project_employee as pe left join project as pr on pe.project_id=pr.id "
				+ "left join employee as e on pe.employee_id=e.id "
				+ "where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if(projectId != null){
			sql += " and pe.project_id=?";
			conditions.add(projectId);
		}
		if(employeeId != null){
			sql += " and pe.employee_id=?";
			conditions.add(employeeId);
		}
		if (!Strings.isNullOrEmpty(month)) {
			sql += " and pe.`month`=?";
			conditions.add(month);
		}
		sql += " order by pe.id desc limit ?,?";
		conditions.add(offset);
		conditions.add(rows);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<ProjectEmployeeV> projectEmployeeVs = Lists.newArrayList();
		if (list != null) {
			for (HashMap<String, Object> map : list) {
				ProjectEmployeeV p = this.makeProjectEmployeeV(map);
				projectEmployeeVs.add(p);
			}
		}
		return projectEmployeeVs;
	}

	@Override
	public int count(Long projectId, Long employeeId, String month) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from project_employee where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if(projectId != null){
			sql += " and project_id=?";
			conditions.add(projectId);
		}
		if(employeeId != null){
			sql += " and employee_id=?";
			conditions.add(employeeId);
		}
		if (!Strings.isNullOrEmpty(month)) {
			sql += " and `month`=?";
			conditions.add(month);
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	private ProjectEmployeeV makeProjectEmployeeV(HashMap<String, Object> map){
		ProjectEmployeeV p = new ProjectEmployeeV();
		p.setApportion((Double)map.get("project_employee.apportion"));
		p.setEmployeeId((Long)map.get("project_employee.employee_id"));
		p.setEmployeeName((String)map.get("employee.name"));
//		p.setFunctionsId((Long)map.get("project_employee.functions_id"));
		p.setId((Long)map.get("project_employee.id"));
		p.setProjectId((Long)map.get("project_employee.project_id"));
		p.setProjectName((String)map.get("project.name"));
		p.setIsBusiness((String)map.get("project.is_business"));
		p.setMonth((String)map.get("project_employee.month"));
		p.setSpecialAlgorithm((String)map.get("project.special_algorithm"));
		p.setDepartmentName((String)map.get("department.name")+"("+(String)map.get("company.name")+")");
		p.setIsFulltime((String)map.get("employee.is_fulltime"));
		p.setLeaveJobDate((String)map.get("employee.leave_job_date"));
		return p;
	}

	@Override
	public List<ProjectEmployeeV> find(Long projectId, Long employeeId, String month) {
		// TODO Auto-generated method stub
		String sql = "select * from project_employee as pe left join project as pr on pe.project_id=pr.id "
				+ "left join employee as e on pe.employee_id=e.id "
				+ "where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if(projectId != null){
			sql += " and pe.project_id=?";
			conditions.add(projectId);
		}
		if(employeeId != null){
			sql += " and pe.employee_id=?";
			conditions.add(employeeId);
		}
		if (!Strings.isNullOrEmpty(month)) {
			sql += " and pe.`month`=?";
			conditions.add(month);
		}
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<ProjectEmployeeV> projectEmployeeVs = Lists.newArrayList();
		if (list != null) {
			for (HashMap<String, Object> map : list) {
				ProjectEmployeeV p = this.makeProjectEmployeeV(map);
				projectEmployeeVs.add(p);
			}
		}
		return projectEmployeeVs;
	}

	@Override
	public ProjectEmployeeV getByProjectIdAndEmployeeId(Long projectId,
			Long employeeId, String month) {
		// TODO Auto-generated method stub
		String sql = "select * from project_employee as pe left join project as pr on pe.project_id=pr.id "
				+ "left join employee as e on pe.employee_id=e.id "
				+ "where pe.project_id=? and pe.employee_id=? and pe.`month`=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(projectId);
		conditions.add(employeeId);
		conditions.add(month);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		ProjectEmployeeV projectEmployeeV = null;
		if (list != null && list.size() !=0) {
			projectEmployeeV = this.makeProjectEmployeeV(list.get(0));
		}
		return projectEmployeeV;
	}

	@Override
	public List<ProjectEmployeeV> findByMonth(String month) {
		// TODO Auto-generated method stub
		String sql = "select * from project_employee as pe left join project as pr on pe.project_id=pr.id "
				+ "left join employee as e on pe.employee_id=e.id "
				+ "left join department as d on e.department_id=d.id "
				+ "left join company as c on d.company_id=c.id "
				+ "where pe.`month`=? and (e.status='on_job' or (e.status='leave_job' and locate('"+month+"',e.leave_job_date)>0))";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(month);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<ProjectEmployeeV> projectEmployeeVs = Lists.newArrayList();
		for (HashMap<String, Object> map : list) {
			ProjectEmployeeV projectEmployeeV = this.makeProjectEmployeeV(map);
			projectEmployeeVs.add(projectEmployeeV);
		}
		return projectEmployeeVs;
	}

}
