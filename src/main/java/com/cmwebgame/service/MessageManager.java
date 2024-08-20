package com.cmwebgame.service;

import com.cmwebgame.entities.Message;
import com.cmwebgame.entities.vo.PageVo;

public interface MessageManager {
	
	public Message save(Message message);
	
	public Message getById(Long id);
	/**
	 * 获取指定月份的excel导入结果
	 * @param month
	 * @return
	 */
	public Message getByMonth(String month);
	/**
	 * 获取指定employeeId的消息分页
	 * @param employeeId
	 * @return
	 */
	public PageVo<Message> findPageByKindId(Long employeeId, int pageNum, int rows);
	/**
	 * 获取指定kindId的消息数量
	 * @param kind
	 * @return
	 */
	public int countByKindId(Long employeeId);
	/**
	 * 获取指定kindId的未读消息数量
	 * @param kind
	 * @return
	 */
	public int countNotReadByKindId(Long employeeId);

}
