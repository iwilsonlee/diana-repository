package com.cmwebgame.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.util.Base64;

import com.cmwebgame.cache.CacheEngine;
import com.cmwebgame.cache.Cacheable;
import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.entities.Company;
import com.cmwebgame.entities.Module;
import com.cmwebgame.entities.ModuleContent;
import com.google.common.collect.Lists;

public class CompanyRepository implements Cacheable {
	
	private static Logger logger = Logger.getLogger(CompanyRepository.class);
	
	private static final String FQN = "company";
	
	private static final String FQN_LIST = FQN + "_list";
	
	private static CacheEngine cache;

	@Override
	public void setCacheEngine(CacheEngine engine) {
		// TODO Auto-generated method stub
		cache = engine;
	}

	
	/**
	 * 保存company到cache里(包括新增和更新)
	 * @param config
	 */
	public static void save(Company company){
		synchronized(FQN){
			cache.add(FQN, company.getId().toString(), company);
			cache.add(FQN, Base64.encode(company.getName().getBytes()).toString(), company);
		}
	}
	
	/**
	 * 从cache里获取指定id的company，若没有则从db获取
	 * @param key
	 * @return
	 */
	public static Company getById(Long id){
		Object object = cache.get(FQN, id.toString());
		Company company = null;
		if (object != null) {
			company = (Company)object;
			if(company != null){
				if(logger.isDebugEnabled()){
					logger.debug("company getById in cache is" + company.getName());
				}
			}
		}else{
			company = DataAccessDriver.getInstance().newCompanyManager().getById(id);
			if(company!=null){
				save(company);
			}
			
		}
		return company;
	}
	/**
	 * 从cache里获取指定Name的company，若没有则从db获取
	 * @param key
	 * @return
	 */
	public static Company getByName(String name){
		String cacheName = Base64.encode(name.getBytes()).toString();
		Object object = cache.get(FQN, cacheName);
		Company company = null;
		if (object != null) {
			company = (Company)object;
			if(company != null){
				if(logger.isDebugEnabled()){
					logger.debug("company getByName in cache is" + company.getName());
				}
			}
		}else{
			company = DataAccessDriver.getInstance().newCompanyManager().getByName(name);
			if(company!=null){
				save(company);
			}
		}
		return company;
	}
	
	/**
	 * 从cache里获取Company列表，若没有则从db获取
	 * @return
	 */
	public static List<Company> findAll(){
		Object object = cache.get(FQN_LIST);
		List<Long> list = null;
		if (object != null) {
			list = (List<Long>)object;
			if(list != null){
				if(logger.isDebugEnabled()){
					logger.debug("Module.findAll in cache size is" + list.size());
				}
			}
		}else {
			list = DataAccessDriver.getInstance().newCompanyManager().findByAll();
			synchronized(FQN_LIST){
				cache.add(FQN_LIST, list);
			}
		}
		List<Company> companies = Lists.newArrayList();
		for(Long id: list){
			Company company = getById(id);
			companies.add(company);
		}
		return  companies;
	}
	
	
	
	/**
	 * 清除所有module相关的cache
	 */
	public static void clear(){
		synchronized(FQN){
			List<Company> list = findAll();
			for(Company company : list){
				removeById(company.getId());
				removeByName(company.getName());
			}
			removeList();
		}
		
	}
	
	/**
	 * 清除cache里指定id的Company
	 * @param key
	 */
	public static void removeById(Long id){
		synchronized(FQN){
			if(cache != null){
				cache.remove(FQN, id.toString());
			}
		}
	}
	/**
	 * 清除cache里指定name的Company
	 * @param key
	 */
	public static void removeByName(String name){
		synchronized(FQN){
			if(cache != null){
				name = Base64.encode(name.getBytes()).toString();
				cache.remove(FQN, name);
			}
		}
	}
	
	/**
	 * 清除cache里的module列表
	 */
	public static void removeList(){
		List<Company> modules = findAll();
		for(Company module : modules){
			removeById(module.getId());
			removeByName(module.getName());
		}
		synchronized(FQN_LIST){
			if(cache != null){
				cache.remove(FQN_LIST);
			}
		}
	}
	
	
	
}
