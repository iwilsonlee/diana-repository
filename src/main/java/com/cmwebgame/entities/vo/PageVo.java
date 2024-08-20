package com.cmwebgame.entities.vo;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
/**
 * 分页器
 * @author wilson
 *
 * @param <T>
 */
public class PageVo<T> {
	/**
	 * 页码
	 */
	private int pageNum;
	/**
	 * 页码总数
	 */
	private int pageCount;
	/**
	 * 总数
	 */
	private int count;
	/**
	 * 偏移
	 */
	private int offset;
	/**
	 * 数量
	 */
	private int rows;
	/**
	 * 数据
	 */
	private List<T> list;
	/**
	 * 页码HTML
	 */
	private String pageNumHtml;
	/**
	 * 参数
	 */
	private Map<String, String> args = Maps.newHashMap();

	public PageVo(int pageNum) {
		this.pageNum = pageNum;
	}
	
	public int getPageNum() {
		if(this.pageNum <= 0){
			this.pageNum = 1;
			return 1;
		}else {
			return pageNum;
		}
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageCount() {
		this.pageCount = ((this.getCount() - 1) / this.getRows()) + 1;
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOffset() {
		this.offset = (this.getPageNum() -1) * this.getRows();
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String getPageNumHtml() {
		return pageNumHtml;
	}

	public void setPageNumHtml(String pageNumHtml) {
		this.pageNumHtml = pageNumHtml;
	}

	public Map<String, String> getArgs() {
		return args;
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
	}

}
