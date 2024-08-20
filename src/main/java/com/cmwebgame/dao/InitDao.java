package com.cmwebgame.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cmwebgame.GPortalExecutionContext;
import com.cmwebgame.connection.ConnectionManager;
/**
 * 初始化Dao 数据库连接
 * @author wilson
 *
 * @param <E>
 */
public class InitDao<E> extends BaseDao<E> {
	
	private Logger logger = Logger.getLogger(InitDao.class);

	@Override
	protected Connection getConnection() {
		// TODO Auto-generated method stub
		try {
			if(super.connection == null || super.connection.isClosed()){
				if(super.connection != null){
					logger.debug("connection isClosed:"+super.connection.isClosed());
				}else {
					logger.debug("connection is:"+super.connection);
				}
				try {
					super.connection = GPortalExecutionContext.getConnection();
				} catch (Exception e) {
					// TODO: handle exception
					super.connection = ConnectionManager.getImplementation().getConnection();
				}
				
			}else {
				logger.debug("connection2 isClosed:"+super.connection.isClosed());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.connection;
	}
	
	@Override
	public void setConnection(Connection connection) {
		// TODO Auto-generated method stub
		super.connection = connection;
	}

}
