package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Authority;
import com.cmwebgame.entities.vo.AuthorityV;
import com.cmwebgame.entities.vo.PageVo;

public interface AuthorityManager {
	
	public Authority save(Authority authority);
	
	public Authority getById(Long id);
	
	public AuthorityV getAuthorityVById(Long id);
	
	/**
	 * 获取权限分页，employeeId可为null，当positionId为null时，获取的是所有的权限分页
	 * @param employeeId
	 * @param pageNum
	 * @param rows
	 * @return
	 */
	public PageVo<AuthorityV> findByPage(Long employeeId, int pageNum, int rows);
	/**
	 * employeeId可为null，当employeeId为null时，获取的是所有的权限分页
	 * @param employeeId
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<AuthorityV> find(Long employeeId, int offset, int rows);
	
	public List<Long> findByEmployeeId(Long employeeId);
	
	public List<Long> findByEmployeeIdAndFatherModuleId(Long employeeId, Long fatherModuleId);
	/**
	 * employeeId可为null，当employeeId为null时，获取的是所有的权限数量
	 * @param employeeId
	 * @return
	 */
	public int count(Long employeeId);

}
