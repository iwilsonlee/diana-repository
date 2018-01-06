package com.cmwebgame.repo;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cmwebgame.connection.ConnectionManager;
import com.cmwebgame.dao.portal.DepartmentDao;
import com.cmwebgame.dao.portal.EmployeeDao;
import com.cmwebgame.dao.portal.ModuleDao;
import com.cmwebgame.entities.Employee;
import com.cmwebgame.entities.vo.DepartmentV;
import com.cmwebgame.entities.vo.EmployeeV;
import com.cmwebgame.entities.vo.ModuleV;
import com.cmwebgame.entities.vo.PageVo;
import com.cmwebgame.service.DepartmentManager;
import com.cmwebgame.service.EmployeeManager;
import com.cmwebgame.service.ModuleManager;

public class TestDepartmentDao {
	
    private Connection connection;
	
	private boolean useConn = true;
	
	@Before
	public void before(){
		if(useConn){
			if(this.connection == null){
				PooledDruidConnection pooledDruidConnection = new PooledDruidConnection();
				pooledDruidConnection.init();
				ConnectionManager.createInstance(pooledDruidConnection);
				this.connection = ConnectionManager.getImplementation().getConnection();
			}
		}
	}

//	@Test
	public void testGetById() {
		DepartmentManager departmentManager = new DepartmentDao();
		DepartmentV departmentV = departmentManager.getById(new Long(1));
		assertNotNull(departmentV);
	}
	
//	@Test
	public void testCount(){
		DepartmentManager departmentManager = new DepartmentDao();
		int count = departmentManager.count();
		assertEquals(1, count);
	}
	
//	@Test
	public void testFind(){
		EmployeeManager employeeManager = new EmployeeDao();
		List<Employee> list = employeeManager.findLikeName("李");
		int c=list.size();
		System.out.println(c);
	}
	
	@After
	public void after(){
		try {
			ConnectionManager.getImplementation().realReleaseAllConnections();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
