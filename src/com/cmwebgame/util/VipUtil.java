package com.cmwebgame.util;

import org.apache.log4j.Logger;

//import com.cmwebgame.repository.AccountRepository;

public class VipUtil {
	private static final Logger logger = Logger.getLogger(VipUtil.class);

	/**
	 * 檢查是否為VIP：帳戶曾經儲值過的總金額達$5000
	 * 2012/08/01 改成 $10000
	 * 2013/01/04 改成 $100000
	 * 2013/02/22 改成 $80000
	 * 2013/05/08 改成 $50000
	 * 
	 * @return 
	 */
	public static Boolean isVip(Long memberId) {
		/*
		try { 
			if (AccountRepository.getAccountMoneyAmountByMemberId(memberId).intValue() >= 50000) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Caught exception in check isVip: " + e);
		}
		return false;*/
		return false;
	}

}
