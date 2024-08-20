package com.cmwebgame.service;

import java.util.List;

import com.cmwebgame.entities.Position;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.entities.vo.PositionV;

public interface PositionManager {
	
	public Position save(Position position);
	
	public Position getById(Long id);
	
	public PageVo<Position> findByPage(int pageNum, int rows, String orderBy, String orderName);
	
	public List<Position> find(int offset, int rows, String orderBy, String orderName);
	
	public List<Position> findAll();
	
	public int count();

}
