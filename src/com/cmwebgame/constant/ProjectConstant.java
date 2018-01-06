package com.cmwebgame.constant;

public class ProjectConstant {
	
	public static enum Business {
		/**
		 * 是
		 */
		yes, /**
		 * 否
		 */
		no,
	};
	
	public static String[] businessNames = {"是","否"};
	
	public static enum SpecialAlgorithm {
		/**
		 * 依公司人数分摊
		 */
		person_count, /**
		 * 依项目业绩分摊
		 */
		sales,
	};
	
	public static String[] specialAlgorithmNames = {"依公司人数分摊","依项目业绩分摊"};
	
	public static enum Status {
		/**
		 * 上架
		 */
		on, /**
		 * 下架
		 */
		off,
		
	};
	
	public static String[] statusNames = {"上架","下架"};
}
