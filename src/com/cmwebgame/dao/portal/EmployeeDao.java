package com.cmwebgame.dao.portal;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import com.cmwebgame.constant.EmployeeConstant;
import com.cmwebgame.constant.EmployeeConstant.Fulltime;
import com.cmwebgame.dao.InitDao;
import com.cmwebgame.entities.Employee;
import com.cmwebgame.entities.vo.EmployeeV;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.EmployeeManager;
import com.cmwebgame.util.DataTimeUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class EmployeeDao extends InitDao<Employee> implements EmployeeManager {

	@Override
	public Employee save(Employee employee) {
		// TODO Auto-generated method stub
		Preconditions.checkNotNull(employee);
		if (employee.getId() == null) {
			return this.add(employee);
		}else {
			return this.update(employee);
		}
	}
	
	private Employee add(Employee employee){
		int id = this.insert(employee);
		if (id!=0) {
			employee.setId(new Long(id));
			return employee;
		}
		return null;
	}
	
	private Employee update(Employee employee){
		if(this.modify(employee)){
			return employee;
		}else {
			return null;
		}
	}

	@Override
	public Employee getById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from employee where id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public PageVo<EmployeeV> findPage(int pageNum, int rows) {
		// TODO Auto-generated method stub
		PageVo<EmployeeV> pageVo = new PageVo<EmployeeV>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.find(offset, rows));
		pageVo.setCount(this.count());
		
		return pageVo;
	}
	
	@Override
	public PageVo<EmployeeV> findPageByConditions(HashMap<String, Object> conditions, int pageNum, int rows, String orderBy, String orderName, boolean isAllStatus) {
		// TODO Auto-generated method stub
		PageVo<EmployeeV> pageVo = new PageVo<EmployeeV>(pageNum);
		pageVo.setRows(rows);
		int offset = pageVo.getOffset();
		pageVo.setList(this.findByConditions(conditions, offset, rows, orderBy, orderName,isAllStatus));
		pageVo.setCount(this.count(conditions, isAllStatus));
		
		return pageVo;
	}
	
	public List<EmployeeV> findByConditions(HashMap<String, Object> conditionsMap, int offset, int rows, String orderBy, String orderName, boolean isAllStatus){
		String thisMonth = DataTimeUtil.getThisMonth();
		String sql = "select * from employee as e "
				+ "left join department as d on e.department_id=d.id "
				+ "left join company as c on e.company_id=c.id "
				+ "left join position as p on e.position_id=p.id "
				+ "left join functions as f on e.functions_id=f.id "
				+ "left join (select count(*) as p_count,pe.employee_id as employee_id from project_employee as pe where pe.`month`=? group by pe.employee_id) as t"
				+ " on e.id=t.employee_id where 1=1";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(thisMonth);
		if ((conditionsMap!=null && conditionsMap.size()!=0)) {
			Iterator<Entry<String, Object>> iterator = conditionsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)iterator.next();
				String key = entry.getKey();
				
				if(!isAllStatus){
					if (!key.equals("status")) {
						Object value = entry.getValue();
						sql += " and e."+key+"=?";
						conditions.add(value);
					}
				}else {
					Object value = entry.getValue();
					sql += " and e."+key+"=?";
					conditions.add(value);
				}
			}
		}
		
		if (!isAllStatus) {
			sql += " and (e.status='on_job' or (e.status='leave_job' and locate('"+thisMonth+"',e.leave_job_date)>0))";
		}
		
		if (!Strings.isNullOrEmpty(orderBy) && !Strings.isNullOrEmpty(orderName)) {
			sql += " order by e." + orderName + " " +orderBy;
		}else {
			sql += " order by e.id desc";
		}
		
		if(rows!=0){
			sql += "  limit ?,?";
			conditions.add(offset);
			conditions.add(rows);
		}
		
		
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<EmployeeV> employeeVs = Lists.newArrayList();
		
		if (list != null) {
			for(HashMap<String, Object> map : list){
				EmployeeV employeeV = this.makEmployeeV(map);
				employeeVs.add(employeeV);
			}
		}
		return employeeVs;
	}
	
	@Override
	public List<EmployeeV> find(int offset, int rows){
		String thisMonth = DataTimeUtil.getThisMonth();
		String sql = "select * from employee as e "
				+ "left join department as d on e.department_id=d.id "
				+ "left join company as c on e.company_id=c.id "
				+ "left join position as p on e.position_id=p.id "
				+ "left join functions as f on e.functions_id=f.id "
				+ "left join (select count(*) as p_count,pe.employee_id as employee_id from project_employee as pe where pe.`month`=? group by pe.employee_id) as t"
				+ " on e.id=t.employee_id " + " order by e.id desc";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(thisMonth);
		if(rows!=0){
			sql += "  limit ?,?";
			conditions.add(offset);
			conditions.add(rows);
		}
		
		
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		List<EmployeeV> employeeVs = Lists.newArrayList();
		
		if (list != null) {
			for(HashMap<String, Object> map : list){
				EmployeeV employeeV = this.makEmployeeV(map);
				employeeVs.add(employeeV);
			}
		}
		return employeeVs;
	}
	
	@Override
	public int count(HashMap<String, Object> conditionsMap, boolean isAllStatus){
		String sql = "select count(*) from employee where 1=1";
		List<Object> conditions = Lists.newArrayList();
		if (conditionsMap!=null && conditionsMap.size()!=0) {
			Iterator<Entry<String, Object>> iterator = conditionsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)iterator.next();
				String key = entry.getKey();
				if(!isAllStatus){
					if (!key.equals("status")) {
						Object value = entry.getValue();
						sql += " and "+key+"=?";
						conditions.add(value);
					}
				}else {
					Object value = entry.getValue();
					sql += " and "+key+"=?";
					conditions.add(value);
				}
			}
		}
		if (!isAllStatus) {
			String thisMonth = DataTimeUtil.getThisMonth();
			sql += " and (`status`='on_job' or (`status`='leave_job' and locate('"+thisMonth+"',`leave_job_date`)>0))";
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	public int count(){
		String sql = "select count(*) from employee";
		List<Object> conditions = Lists.newArrayList();
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	public int countByIsFulltime(String isFulltime){
		String sql = "select count(*) from employee where is_fulltime=? and status=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(isFulltime);
		conditions.add(EmployeeConstant.Status.on_job.name());
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public Employee getByEmail(String email) {
		// TODO Auto-generated method stub
		String sql = "select * from employee where email=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(email);
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return (list==null||list.size()==0)?null:list.get(0);
	}
	
	private EmployeeV makEmployeeV(HashMap<String, Object> map){
		EmployeeV employeeV = new EmployeeV();
		employeeV.setBirthday((Date)map.get("employee.birthday"));
		employeeV.setCompanyId((Long)map.get("employee.company_id"));
		employeeV.setCompanyName((String)map.get("company.name"));
		employeeV.setCreateTime((Timestamp)map.get("employee.create_time"));
		employeeV.setDepartmentId((Long)map.get("employee.department_id"));
		employeeV.setDepartmentName((String)map.get("department.name")+"("+employeeV.getCompanyName()+")");
		employeeV.setEmail((String)map.get("employee.email"));
		employeeV.setEname((String)map.get("employee.ename"));
		employeeV.setId((Long)map.get("employee.id"));
		employeeV.setIsFulltime((String)map.get("employee.is_fulltime"));
		employeeV.setJobNumber((String)map.get("employee.job_number"));
		employeeV.setLastLoginTime((Timestamp)map.get("employee.last_login_time"));
		employeeV.setName((String)map.get("employee.name"));
		employeeV.setPassword((String)map.get("employee.password"));
		employeeV.setPositionId((Long)map.get("employee.position_id"));
		employeeV.setPositionName((String)map.get("position.name"));
		employeeV.setFunctionsId((Long)map.get("employee.functions_id"));
		employeeV.setFunctionsName((String)map.get("functions.name"));
		employeeV.setPositionMemo((String)map.get("employee.position_memo"));
		employeeV.setStatus((String)map.get("employee.status"));
		employeeV.setGender((String)map.get("employee.gender"));
		employeeV.setEntryDate((String)map.get("employee.entry_date"));
		employeeV.setLeaveJobDate((String)map.get("employee.leave_job_date"));
		Object object = map.get(".p_count");
		employeeV.setProjectCount(object==null?0:(Long)object);
		return employeeV;
		
	}

	@Override
	public List<Employee> findLikeName(String name) {
		// TODO Auto-generated method stub
		name = name+"%";
		String sql = "select * from employee where name like ?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(name);
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return list;
	}

	@Override
	public List<Employee> findByName(String name) {
		// TODO Auto-generated method stub
		String sql = "select * from employee where name=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(name);
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return list;
	}

	@Override
	public EmployeeV getEmployeeVById(Long id) {
		// TODO Auto-generated method stub
		String sql = "select * from employee as e "
				+ "left join department as d on e.department_id=d.id "
				+ "left join company as c on e.company_id=c.id "
				+ "left join position as p on e.position_id=p.id "
				+ "left join functions as f on e.functions_id=f.id "
				+ "left join (select count(*) as p_count,pe.employee_id as employee_id from project_employee as pe group by pe.employee_id) as t"
				+ " on e.id=t.employee_id where e.id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(id);
		List<HashMap<String, Object>> list = this.getResultSetByCondition(sql, conditions, HashMap.class);
		return (list==null||list.size()==0)?null:makEmployeeV(list.get(0));
	}

	@Override
	public Employee getByJobNumber(String jobNumber) {
		// TODO Auto-generated method stub
		String sql = "select * from employee where job_number=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(jobNumber);
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return (list==null||list.size()==0)?null:list.get(0);
	}

	@Override
	public int countByCompanyId(Long companyId, String employeeStatus) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from employee where company_id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		if (employeeStatus != null) {
			sql += " and `status`=?";
			conditions.add(employeeStatus);
		}
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}

	@Override
	public List<Employee> findByCompanyId(Long companyId ,String employeeStatus) {
		// TODO Auto-generated method stub
		String sql = "select * from employee where company_id=?";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		if (employeeStatus != null) {
			sql += " and `status`=?";
			conditions.add(employeeStatus);
		}
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return list;
	}
	
	@Override
	public int countByCompanyIdAndMonth(Long companyId, String month) {
		// TODO Auto-generated method stub
		DateTime dateTime = DateTime.parse(month).dayOfMonth().withMaximumValue();
		String maxMonthDayStr = dateTime.toString("yyyy-MM-dd");
		String sql = "select count(*) from employee where company_id=? and is_fulltime='"+Fulltime.yes.name()+"' and entry_date<='"+maxMonthDayStr+"' "
				+ " and (`status`='"+EmployeeConstant.Status.on_job.name() +"' "
				+ "or (`status`='"+EmployeeConstant.Status.leave_job.name() +"' and locate('"+month+"',leave_job_date)>0))";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		List<Integer> list = this.getResultSetByCondition(sql, conditions, Integer.class);
		return (list==null||list.size()==0)?0:list.get(0);
	}
	
	@Override
	public List<Employee> findByCompanyIdAndMonth(Long companyId ,String month) {
		// TODO Auto-generated method stub
		DateTime dateTime = DateTime.parse(month).dayOfMonth().withMaximumValue();
		String maxMonthDayStr = dateTime.toString("yyyy-MM-dd");
		String sql = "select * from employee where company_id=? and is_fulltime='"+Fulltime.yes.name()+"' and entry_date<='"+maxMonthDayStr+"'"
				+ " and (`status`='"+EmployeeConstant.Status.on_job.name() +"' "
						+ "or (`status`='"+EmployeeConstant.Status.leave_job.name() +"' and locate('"+month+"',leave_job_date)>0))";
		List<Object> conditions = Lists.newArrayList();
		conditions.add(companyId);
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return list;
	}
	
	@Override
	public List<Employee> findByMonth(String month) {
		// TODO Auto-generated method stub
		DateTime dateTime = DateTime.parse(month).dayOfMonth().withMaximumValue();
		String maxMonthDayStr = dateTime.toString("yyyy-MM-dd");
		String sql = "select * from employee where is_fulltime='"+Fulltime.yes.name()+"' and entry_date<='"+maxMonthDayStr+"' "
				+ " and (`status`='"+EmployeeConstant.Status.on_job.name() +"' "
				+ "or (`status`='"+EmployeeConstant.Status.leave_job.name() +"' and locate('"+month+"',leave_job_date)>0))";
		List<Object> conditions = Lists.newArrayList();
		List<Employee> list = this.getResultSetByCondition(sql, conditions);
		
		return list;
	}

	@Override
	public List<Long> findByAll() {
		// TODO Auto-generated method stub
		String sql = "select id from employee";
		List<Object> conditions = Lists.newArrayList();
		List<Long> list = this.getResultSetByCondition(sql, conditions, Long.class);
		
		return list;
	}
}
