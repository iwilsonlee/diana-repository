package com.cmwebgame.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.cmwebgame.entities.Apportionment;
import com.cmwebgame.entities.vo.PageVo;

public interface ApportionmentManager {
	
	public Apportionment save(Apportionment apportionment);
	
	public Apportionment getById(Long id);
	/**
	 * 根据项目id、公司Id，分摊名称，月份获取分摊内容
	 * @param projectId
	 * @param companyId
	 * @param name
	 * @param month
	 * @return
	 */
	public Apportionment getByProjectIdAndNameAndMonth(Long projectId, Long companyId, String name, String month);
	
	public List<Apportionment> findByMonth(String month);
	
	public List<Apportionment> findByProjectIdAndMonth(Long projectId, Long companyId, String month);
	/**
	 * 获取指定公司指定月份的分摊名称列表
	 * @param companyId
	 * @param month
	 * @return
	 */
	public List<String> findNameByMonth( Long companyId,String month);
	/**
	 * 获取指定公司指定月份的分摊数据，按照公司名为key放到LinkedHashMap中
	 * @param companyId
	 * @param month
	 * @return
	 */
	public LinkedHashMap<String, List<Apportionment>> findMapByMonth(Long companyId,String month);
	/**
	 * 获取指定公司指定月份的分摊数据，按照公司名为key放到LinkedHashMap中
	 * @param companyId
	 * @param month
	 * @param projectStatus  项目状态，为空时忽略此查询条件
	 * @return
	 */
	public LinkedHashMap<String, List<Apportionment>> findMapByMonthAndProjectStatus(Long companyId, String month, String pojectStatus);
	/**
	 * 查找指定月份的分摊列表分页，月份可以为null，当为null时表示查找全部
	 * @param month
	 * @return
	 */
	public PageVo<Apportionment> findPageByMonth(String month, int pageNum, int rows);
	/**
	 * 查找指定月份的分摊数目，当month=null时表示查找全部数目
	 * @param month
	 * @return
	 */
	public int count(String month);
	/**
	 * 把指定月份指定来源的Apportionment的amount设置为0
	 * @param month
	 * @param fromExcel
	 * @return
	 */
	public boolean clearAmountByMonth(String month, String fromExcel);

}
