package com.cmwebgame.constant;

public class MessageConstant {
	
	public static enum Kind {
		/**
		 * 系统消息
		 */
		system, /**
		 * 个人消息
		 */
		employee,
	};
	
	public static String[] kindNames = {"系统消息","个人消息"};
	
	public static enum IsReaded {
		/**
		 * 是
		 */
		yes, /**
		 * 否
		 */
		no,
	};
	
	public static String[] IsReadedNames = {"是","否"};
	
	
}
