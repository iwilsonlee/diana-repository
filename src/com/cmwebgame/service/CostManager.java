package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Cost;
import com.cmwebgame.entities.vo.PageVo;

public interface CostManager {
	
	public Cost save(Cost cost);
	
	/**
	 * 使用事务批量保存或更新Cost
	 * @param costs
	 * @return
	 */
	public boolean saveOnTransaction(List<Cost> costs);
	
	public Cost getById(Long id);
	
	public Cost getByKindAndKindIdAndNameAndMonth(String kind, Long kindId, String name, String month);
	
	public PageVo<Cost> findPage(int pageNum, int rows);
	
	public List<Cost> find(int offset, int rows);
	
	public int count();
	
	/**
	 * 获取指定月份的成本列表
	 * @param month
	 * @return
	 */
	public List<Cost> findByMonth(String month);
	/**
	 * 把指定月份指定来源的cost的amount设置为0
	 * @param month
	 * @param fromExcel
	 * @return
	 */
	public boolean clearAmountByMonth(String month, String fromExcel);
	
}
