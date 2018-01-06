package com.cmwebgame.handle.impl;

import org.apache.log4j.Logger;

import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.exceptions.PortalException;
import com.cmwebgame.handle.DataDriverHandle;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.SystemGlobals;

public class DataDriverHandleImpl implements DataDriverHandle {

	private static final Logger logger = Logger.getLogger(DataDriverHandleImpl.class);
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		String driver = SystemGlobals.getValue(ConfigKeys.DAO_DRIVER);

		logger.info("Loading JDBC driver " + driver);

		try {
			Class<?> c = Class.forName(driver);
			DataAccessDriver d = (DataAccessDriver)c.newInstance();
			DataAccessDriver.init(d);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
	}

}
