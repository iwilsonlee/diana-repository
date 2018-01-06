package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Functions;
import com.cmwebgame.entities.vo.PageVo;

public interface FunctionsManager {
	
	public Functions save(Functions functions);
	
	public Functions getById(Long id);
	
	public PageVo<Functions> findByPage(int pageNum, int rows, String orderBy, String orderName);
	
	public List<Functions> find(int offset,int rows, String orderBy, String orderName);
	
	public int count();
	
	

}
