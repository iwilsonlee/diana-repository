package com.cmwebgame.repository;

import java.util.List;

import org.apache.log4j.Logger;

import com.cmwebgame.cache.CacheEngine;
import com.cmwebgame.cache.Cacheable;
import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.entities.Module;
import com.cmwebgame.entities.ModuleContent;
import com.google.common.collect.Lists;

public class ModuleRepository implements Cacheable {
	
	private static Logger logger = Logger.getLogger(ModuleRepository.class);
	
	private static final String FQN = "module";
	
	private static final String FQN_MODULE_CONTENT = "module_content";
	
	private static final String FQN_LIST = FQN + "_list";
	
	private static final String FQN_MODULE_CONTENT_LIST = FQN_MODULE_CONTENT + "_list";
	
	private static CacheEngine cache;

	@Override
	public void setCacheEngine(CacheEngine engine) {
		// TODO Auto-generated method stub
		cache = engine;
	}

	
	/**
	 * 保存module到cache里(包括新增和更新)
	 * @param config
	 */
	public static void save(Module module){
		synchronized(FQN){
			cache.add(FQN, module.getId().toString(), module);
		}
	}
	public static void saveByUrl(Module module){
		synchronized(FQN){
			cache.add(FQN, module.getUrl(), module);
		}
	}
	
	public static void saveModuleContent(ModuleContent moduleContent){
		synchronized(FQN){
			cache.add(FQN_MODULE_CONTENT, moduleContent.getId().toString(), moduleContent);
		}
	}
	
	/**
	 * 从cache里获取指定id的module，若没有则从db获取
	 * @param key
	 * @return
	 */
	public static Module getById(Long id){
		Object object = cache.get(FQN, id.toString());
		Module module = null;
		if (object != null) {
			module = (Module)object;
			if(module != null){
				if(logger.isDebugEnabled()){
					logger.debug("Module getById in cache is" + module.getName());
				}
			}
		}else{
			module = DataAccessDriver.getInstance().newModuleManager().getById(id);
			if (module!=null) {
				save(module);
			}
		}
		return module;
	}
	public static Module getByUrl(String url){
		Object object = cache.get(FQN, url);
		Module module = null;
		if (object != null) {
			module = (Module)object;
			if(module != null){
				if(logger.isDebugEnabled()){
					logger.debug("Module getById in cache is" + module.getName());
				}
			}
		}else{
			module = DataAccessDriver.getInstance().newModuleManager().getByURL(url);
			if (module!=null) {
				save(module);
			}
		}
		return module;
	}
	
	public static ModuleContent getModuleContentById(Long id){
		Object object = cache.get(FQN_MODULE_CONTENT, id.toString());
		ModuleContent moduleContent = null;
		if (object != null) {
			moduleContent = (ModuleContent)object;
			if(moduleContent != null){
				if(logger.isDebugEnabled()){
					logger.debug("ModuleContent getModuleContentById in cache is" + moduleContent.getName());
				}
			}
		}else{
			moduleContent = DataAccessDriver.getInstance().newModuleContentManager().getById(id);
			if (moduleContent!=null) {
				saveModuleContent(moduleContent);
			}
		}
		return moduleContent;
	}
	
	/**
	 * 从cache里获取module列表，若没有则从db获取
	 * @return
	 */
	public static List<Module> findAll(){
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
			list = DataAccessDriver.getInstance().newModuleManager().findByAll();
			synchronized(FQN_LIST){
				cache.add(FQN_LIST, list);
			}
		}
		List<Module> modules = Lists.newArrayList();
		for(Long id: list){
			Module module = getById(id);
			modules.add(module);
		}
		return  modules;
	}
	
	/**
	 * 从cache里获取指定fatherModuleId的module列表，若没有则从db获取
	 * @return
	 */
	public static List<Module> findAllByFatherModuleId(Long fatherModuleId){
		Object object = cache.get(FQN_LIST+"_"+fatherModuleId);
		List<Long> list = null;
		if (object != null) {
			list = (List<Long>)object;
			if(list != null){
				if(logger.isDebugEnabled()){
					logger.debug("Module.findAll in cache size is" + list.size());
				}
			}
		}else {
			list = DataAccessDriver.getInstance().newModuleManager().findByFatherModuleId(fatherModuleId);
			synchronized(FQN_LIST+"_"+fatherModuleId){
				cache.add(FQN_LIST+"_"+fatherModuleId, list);
			}
		}
		List<Module> modules = Lists.newArrayList();
		for(Long id: list){
			Module module = getById(id);
			modules.add(module);
		}
		return  modules;
	}
	
	public static List<ModuleContent> findAllByModuleId(Long moduleId){
		Object object = cache.get(FQN_MODULE_CONTENT_LIST+"_"+moduleId);
		List<Long> list = null;
		if (object != null) {
			list = (List<Long>)object;
			if(list != null){
				if(logger.isDebugEnabled()){
					logger.debug("ModuleContent.findAllByModuleId in cache size is" + list.size());
				}
			}
		}else {
			list = DataAccessDriver.getInstance().newModuleContentManager().findAllByModuleId(moduleId);
			synchronized(FQN_MODULE_CONTENT_LIST+"_"+moduleId){
				cache.add(FQN_MODULE_CONTENT_LIST+"_"+moduleId, list);
			}
		}
		List<ModuleContent> moduleContents = Lists.newArrayList();
		for(Long id: list){
			ModuleContent moduleContent = getModuleContentById(id);
			moduleContents.add(moduleContent);
		}
		return  moduleContents;
	}
	
	public static List<ModuleContent> findAllByUrl(String url){
		Object object = cache.get(FQN_MODULE_CONTENT_LIST+"_"+url);
		List<Long> list = null;
		if (object != null) {
			list = (List<Long>)object;
			if(list != null){
				if(logger.isDebugEnabled()){
					logger.debug("ModuleContent.findAllByModuleId in cache size is" + list.size());
				}
			}
		}else {
			list = DataAccessDriver.getInstance().newModuleContentManager().findAllByUrl(url);
			synchronized(FQN_MODULE_CONTENT_LIST+"_"+url){
				cache.add(FQN_MODULE_CONTENT_LIST+"_"+url, list);
			}
		}
		List<ModuleContent> moduleContents = Lists.newArrayList();
		for(Long id: list){
			ModuleContent moduleContent = getModuleContentById(id);
			moduleContents.add(moduleContent);
		}
		return  moduleContents;
	}
	
	/**
	 * 清除所有module相关的cache
	 */
	public static void clear(){
		synchronized(FQN){
			List<Module> list = findAll();
			for(Module module : list){
				removeListByFatherModuleId(module.getFatherModuleId());
				List<ModuleContent> moduleContents = findAllByModuleId(module.getId());
				for(ModuleContent moduleContent : moduleContents){
					removeModuleContentListByUrl(moduleContent.getUrl());
				}
				removeModuleContentListByModuleId(module.getId());
				removeById(module.getId());
				removeByUrl(module.getUrl());
			}
			removeList();
		}
		
	}
	
	/**
	 * 清除cache里指定key的module
	 * @param key
	 */
	public static void removeById(Long id){
		synchronized(FQN){
			if(cache != null){
				cache.remove(FQN, id.toString());
			}
		}
	}
	public static void removeByUrl(String url){
		synchronized(FQN){
			if(cache != null){
				cache.remove(FQN, url);
			}
		}
	}
	public static void removeModuleContentById(Long id){
		synchronized(FQN_MODULE_CONTENT){
			if(cache != null){
				cache.remove(FQN_MODULE_CONTENT, id.toString());
			}
		}
	}
	
	/**
	 * 清除cache里的module列表
	 */
	public static void removeList(){
		List<Module> modules = findAll();
		for(Module module : modules){
			removeById(module.getId());
		}
		synchronized(FQN_LIST){
			if(cache != null){
				cache.remove(FQN_LIST);
			}
		}
	}
	
	public static void removeListByFatherModuleId(Long fatherModuleId){
		List<Module> modules = findAllByFatherModuleId(fatherModuleId);
		for(Module module : modules){
			removeById(module.getId());
		}
		synchronized(FQN_LIST+"_"+fatherModuleId){
			if(cache != null){
				cache.remove(FQN_LIST+"_"+fatherModuleId);
			}
		}
	}
	
	public static void removeModuleContentListByModuleId(Long moduleId){
		List<ModuleContent> modules =  findAllByModuleId(moduleId);
		for(ModuleContent module : modules){
			removeModuleContentById(module.getId());
		}
		synchronized(FQN_MODULE_CONTENT_LIST+"_"+moduleId){
			if(cache != null){
				cache.remove(FQN_MODULE_CONTENT_LIST+"_"+moduleId);
			}
		}
	}
	
	public static void removeModuleContentListByUrl(String url){
		List<ModuleContent> modules = findAllByUrl(url);
		for(ModuleContent module : modules){
			removeModuleContentById(module.getId());
		}
		synchronized(FQN_MODULE_CONTENT_LIST+"_"+url){
			if(cache != null){
				cache.remove(FQN_MODULE_CONTENT_LIST+"_"+url);
			}
		}
	}
	
}
