package com.cmwebgame.repository;

import java.util.List;

import org.apache.log4j.Logger;

import com.cmwebgame.cache.CacheEngine;
import com.cmwebgame.cache.Cacheable;
import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.entities.Employee;
import com.cmwebgame.entities.Module;
import com.cmwebgame.entities.Position;
import com.cmwebgame.entities.vo.AuthorityV;
import com.google.common.collect.Lists;

public class AuthorityRepository implements Cacheable {
	
	private static Logger logger = Logger.getLogger(AuthorityRepository.class);
	
	private static final String FQN = "authority";
	
	private static final String FQN_LIST = FQN + "_list";
	
	private static final String FQN_PERSONNEL = FQN + "_personnel";
	
	private static CacheEngine cache;

	@Override
	public void setCacheEngine(CacheEngine engine) {
		// TODO Auto-generated method stub
		cache = engine;
	}

	
	/**
	 * 保存authority到cache里(包括新增和更新)
	 * @param config
	 */
	public static void save(AuthorityV authority){
		synchronized(FQN){
			cache.add(FQN, authority.getId().toString(), authority);
		}
	}
	
	/**
	 * 从cache里获取指定id的authority，若没有则从db获取
	 * @param key
	 * @return
	 */
	public static AuthorityV getById(Long id){
		Object object = cache.get(FQN, id.toString());
		AuthorityV authority = null;
		if(object != null){
			authority = (AuthorityV)object;
			if(authority != null){
				if(logger.isDebugEnabled()){
					logger.debug("Authority getById in cache is" + authority.getId());
				}
			}
		}else {
			authority = DataAccessDriver.getInstance().newAuthorityManager().getAuthorityVById(id);
			if (authority!=null) {
				save(authority);
			}
		}
		return authority;
	}
	
	/**
	 * 从cache里获取指定employeeId的authority列表，若没有则从db获取
	 * @return
	 */
	public static List<AuthorityV> findAllByEmployeeId(Long employeeId){
		String theFQN = getFQNList(employeeId);
		Object object = cache.get(theFQN);
		List<Long> list = null;
		if (object != null) {
			list = (List<Long>)object;
			if(list != null){
				if(logger.isDebugEnabled()){
					logger.debug("Authority.findAllByEmployeeId in cache size is" + list.size());
				}
			}
		}else {
			list = DataAccessDriver.getInstance().newAuthorityManager().findByEmployeeId(employeeId);
			synchronized(theFQN){
				cache.add(theFQN, list);
			}
		}
		List<AuthorityV> authorityVs = Lists.newArrayList();
		for(Long id: list){
			AuthorityV authorityV = getById(id);
			authorityVs.add(authorityV);
		}
		return  authorityVs;
	}
	
	/**
	 * 检查指定人员id是否有超级权限
	 * @param employeeId
	 * @return
	 */
	public static boolean checkIsSuperAuthority(Long employeeId){
		List<AuthorityV> list = findAllByEmployeeId(employeeId);
		for(AuthorityV authorityV : list){
			if(authorityV.getModuleUrl() != null && authorityV.getModuleUrl().equals("*")){
    			return true;
    		}
		}
		return false;
	}
	
	public static boolean checkIsPersonnel(Long employeeId){
		Boolean isPersonnel = (Boolean)cache.get(FQN_PERSONNEL, employeeId.toString());
		if (isPersonnel==null) {
			Employee employee = EmployeeRepository.getById(employeeId);
			Position position = DataAccessDriver.getInstance().newPositionManager().getById(employee.getPositionId());
			if (position.getName().equals("人事")) {
				isPersonnel = new Boolean(true);
			}else {
				isPersonnel = new Boolean(false);
			}
			synchronized(FQN_PERSONNEL){
				cache.add(FQN_PERSONNEL, employeeId.toString(), isPersonnel);
			}
		}
		return isPersonnel;
		
	}
	
	/**
	 * 从cache里获取指定employeeId和fatherModuleId的authority列表，若没有则从db获取
	 * @return
	 */
	public static List<AuthorityV> findByEmployeeIdAndFatherModuleId(Long employeeId, Long fatherModuleId){
		String theFQN = getFQNList(employeeId)+"_fm_"+fatherModuleId;
		Object object = cache.get(theFQN);
		List<Long> list = null;
		if (object != null) {
			list = (List<Long>)object;
			if(list != null){
				if(logger.isDebugEnabled()){
					logger.debug("Authority.findByEmployeeIdAndFatherModuleId in cache size is" + list.size());
				}
			}
		}else {
			list = DataAccessDriver.getInstance().newAuthorityManager().findByEmployeeIdAndFatherModuleId(employeeId, fatherModuleId);
			synchronized(theFQN){
				cache.add(theFQN, list);
			}
		}
		List<AuthorityV> authorityVs = Lists.newArrayList();
		for(Long id: list){
			AuthorityV authorityV = getById(id);
			authorityVs.add(authorityV);
		}
		return  authorityVs;
	}
	
	/**
	 * 清除所有authority相关的cache
	 */
	public static void clear(){
		synchronized(FQN){
			List<Long> employeeIds = DataAccessDriver.getInstance().newEmployeeManager().findByAll();
			List<Module> modules = ModuleRepository.findAll();
			for (Long employeeId : employeeIds) {
				if(cache != null){
					removeListByEmployeeId(employeeId);
					for(Module module : modules){
						removeListByEmployeeIdAndFatherModuleId(employeeId, module.getId());
					}
				}
			}
		}
		
	}
	
	/**
	 * 清除cache里指定id的authority
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
	 * 清除cache里指定employeeId的“人事”权限检查结果
	 * @param key
	 */
	public static void removePersonnelByEmployeeId(Long employeeId){
		synchronized(FQN_PERSONNEL){
			if(cache != null){
				cache.remove(FQN_PERSONNEL, employeeId.toString());
			}
		}
	}
	
	/**
	 * 清除cache里指定employeeId的authority列表
	 */
	public static void removeListByEmployeeId(Long employeeId){
		String theFQN = getFQNList(employeeId);
		List<AuthorityV> authorityVs = findAllByEmployeeId(employeeId);
		for(AuthorityV authorityV : authorityVs){
			removeById(authorityV.getId());
		}
		synchronized(theFQN){
			if(cache != null){
				cache.remove(theFQN);
			}
		}
	}
	public static void removeListByEmployeeIdAndFatherModuleId(Long employeeId, Long fatherModuleId){
		String theFQN = getFQNList(employeeId)+"_fm_"+fatherModuleId;
		List<AuthorityV> authorityVs = findByEmployeeIdAndFatherModuleId(employeeId, fatherModuleId);
		for(AuthorityV authorityV : authorityVs){
			removeById(authorityV.getId());
		}
		synchronized(theFQN){
			if(cache != null){
				cache.remove(theFQN);
			}
		}
	}
	
	private static String getFQNList(Long employeeId){
		String theFQN = FQN_LIST+"_"+employeeId.toString();
		return theFQN;
	}
	
}
