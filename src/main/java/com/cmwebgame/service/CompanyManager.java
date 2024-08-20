package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Company;
import com.cmwebgame.entities.vo.PageVo;

public interface CompanyManager {
	
	public Company save(Company company);
	
	public Company getById(Long id);
	
	public Company getByName(String name);
	
	public PageVo<Company> findByPage(int pageNum, int rows, String orderBy, String orderName);
	
	public List<Company> find(int offset, int rows, String orderBy, String orderName);
	
	public int count();
	
	public List<Long> findByAll();
}
