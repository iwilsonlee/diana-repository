package com.cmwebgame.dao.driver;

import com.cmwebgame.service.ApportionmentManager;
import com.cmwebgame.service.AuthorityManager;
import com.cmwebgame.service.CompanyManager;
import com.cmwebgame.service.CostManager;
import com.cmwebgame.service.DepartmentManager;
import com.cmwebgame.service.EmployeeManager;
import com.cmwebgame.service.FunctionsManager;
import com.cmwebgame.service.MemberManager;
import com.cmwebgame.service.MemberSessionManager;
import com.cmwebgame.service.MessageManager;
import com.cmwebgame.service.ModuleContentManager;
import com.cmwebgame.service.ModuleManager;
import com.cmwebgame.service.PerformanceManager;
import com.cmwebgame.service.PositionManager;
import com.cmwebgame.service.ProjectEmployeeManager;
import com.cmwebgame.service.ProjectManager;
import com.cmwebgame.service.SalaryManager;

public abstract class DataAccessDriver {
	
	private static DataAccessDriver driver;

	protected DataAccessDriver() {
	}

	/**
	 * Starts the engine.
	 * This method should be called when the system
	 * is starting. 
	 * 
	 * @param implementation The dao.driver implementation
	 */
	public static void init(DataAccessDriver implementation) {
		driver = implementation;
	}

	/**
	 * Gets a driver implementation instance. 
	 * You MUST use this method when you want a instance
	 * of a valid <code>DataAccessDriver</code>. Never access
	 * the driver implementation directly.  
	 * 
	 * @return <code>DataAccessDriver</code> instance
	 */
	public static DataAccessDriver getInstance() {
		return driver;
	}
	/**
	 * Gets a {@link com.cmwebgame.service.pub.GameManager} instance. 
	 * 
	 */

	public abstract MemberManager newMemberManager();

	public abstract MemberSessionManager newMemberSessionManager();
	
	public abstract AuthorityManager newAuthorityManager();
	
	public abstract CompanyManager newCompanyManager();
	
	public abstract CostManager newCostManager();
	
	public abstract DepartmentManager newDepartmentManager();
	
	public abstract EmployeeManager newEmployeeManager();
	
	public abstract FunctionsManager newFunctionsManager();
	
	public abstract PositionManager newPositionManager();
	
	public abstract ProjectEmployeeManager newProjectEmployeeManager();
	
	public abstract ProjectManager newProjectManager();
	
	public abstract PerformanceManager newPerformanceManager();
	
	public abstract ModuleManager newModuleManager();
	
	public abstract ModuleContentManager newModuleContentManager();
	
	public abstract ApportionmentManager newApportionmentManager();
	
	public abstract MessageManager newmMessageManager();
	
	public abstract SalaryManager newSalaryManager();
	
}
