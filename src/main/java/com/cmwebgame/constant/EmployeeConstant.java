package com.cmwebgame.constant;

public class EmployeeConstant {
	
	public static enum Status {
		/**
		 * 在职
		 */
		on_job, /**
		 * 离职
		 */
		leave_job,
		
	};
	
	public static String[] statusNames = {"在职","离职"};
	
	public static enum Fulltime {
		/**
		 * 是
		 */
		yes, /**
		 * 否
		 */
		no,
	};
	
	public static String[] fulltimeNames = {"是","否"};
	
	public static enum Gender {
		/**
		 * 男
		 */
		man, /**
		 * 女
		 */
		woman,
	};
	
	public static String[] GenderNames = {"男","女"};
}
