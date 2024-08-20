package com.cmwebgame.constant;

public class AuthorityConstant {
	
	public static enum Permission {
		/**
		 * 无权限
		 */
		none, /**
		 * 可读
		 */
		read,
		/**
		 * 可写
		 */
		write,
		
	};
	
	public static String[] permissionNames = {"无权限","可读","可写"};
}
