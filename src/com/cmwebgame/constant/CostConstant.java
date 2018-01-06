package com.cmwebgame.constant;

public class CostConstant {
	
	public static enum Type {
		/**
		 * 项目百分比
		 */
		project_percent, /**
		 * 公司人数
		 */
		person_count,
		/**
		 * 销售业绩
		 */
		sales,
	};
	
	public static String[] typeNames = {"项目百分比","公司人数","销售业绩"};
	
	public static enum Kind {
		/**
		 * 人员
		 */
		employee, /**
		 * 公司
		 */
		company,
	};
	
}
