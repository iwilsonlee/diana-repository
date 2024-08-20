package com.cmwebgame.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.util.Base64;

import com.cmwebgame.cache.CacheEngine;
import com.cmwebgame.cache.Cacheable;
import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.entities.Company;
import com.cmwebgame.entities.Employee;
import com.cmwebgame.entities.Module;
import com.cmwebgame.entities.ModuleContent;
import com.google.common.collect.Lists;

public class EmployeeRepository implements Cacheable {
	
	private static Logger logger = Logger.getLogger(EmployeeRepository.class);
	
	private static final String FQN = "employee";
	private static final String FQN_JON_NUMBER = FQN + "_job_num_";
	
	private static final String FQN_LIST = FQN + "_list";
	
	private static CacheEngine cache;

	@Override
	public void setCacheEngine(CacheEngine engine) {
		// TODO Auto-generated method stub
		cache = engine;
	}

	
	/**
	 * 保存employee到cache里(包括新增和更新)
	 * @param config
	 */
	public static void save(Employee employee){
		synchronized(FQN){
			cache.add(FQN, employee.getId().toString(), employee);
			cache.add(FQN_JON_NUMBER, employee.getJobNumber(), employee);
		}
	}
	
	/**
	 * 从cache里获取指定id的employee，若没有则从db获取
	 * @param key
	 * @return
	 */
	public static Employee getById(Long id){
		Object object = cache.get(FQN, id.toString());
		Employee employee = null;
		if (object != null) {
			employee = (Employee)object;
			if(employee != null){
				if(logger.isDebugEnabled()){
					logger.debug("employee getById in cache is" + employee.getName());
				}
			}
		}else{
			employee = DataAccessDriver.getInstance().newEmployeeManager().getById(id);
			if (employee!=null) {
				save(employee);
			}
		}
		return employee;
	}
	/**
	 * 从cache里获取指定工号的employee，若没有则从db获取
	 * @param key
	 * @return
	 */
	public static Employee getByJobNumber(String jobNumber){
		Object object = cache.get(FQN_JON_NUMBER, jobNumber);
		Employee employee = null;
		if (object != null) {
			employee = (Employee)object;
			if(employee != null){
				if(logger.isDebugEnabled()){
					logger.debug("employee getByJobNumber in cache is" + employee.getName());
				}
			}
		}else{
			employee = DataAccessDriver.getInstance().newEmployeeManager().getByJobNumber(jobNumber);
			if (employee!=null) {
				save(employee);
			}
			
		}
		return employee;
	}
	
	
	
	
	
	
	/**
	 * 清除指定employee相关的cache
	 */
	public static void clear(Employee employee){
		synchronized(FQN){
			cache.remove(FQN, employee.getId().toString());
			cache.remove(FQN_JON_NUMBER, employee.getJobNumber());
		}
		
	}
	

	
	
	
	
}
