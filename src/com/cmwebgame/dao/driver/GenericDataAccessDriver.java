package com.cmwebgame.dao.driver;

import com.cmwebgame.dao.portal.ApportionmentDao;
import com.cmwebgame.dao.portal.AuthorityDao;
import com.cmwebgame.dao.portal.CompanyDao;
import com.cmwebgame.dao.portal.CostDao;
import com.cmwebgame.dao.portal.DepartmentDao;
import com.cmwebgame.dao.portal.EmployeeDao;
import com.cmwebgame.dao.portal.FunctionsDao;
import com.cmwebgame.dao.portal.MemberDao;
import com.cmwebgame.dao.portal.MemberSessionDao;
import com.cmwebgame.dao.portal.MessageDao;
import com.cmwebgame.dao.portal.ModuleContentDao;
import com.cmwebgame.dao.portal.ModuleDao;
import com.cmwebgame.dao.portal.PerformanceDao;
import com.cmwebgame.dao.portal.PositionDao;
import com.cmwebgame.dao.portal.ProjectDao;
import com.cmwebgame.dao.portal.ProjectEmployeeDao;
import com.cmwebgame.dao.portal.SalaryDao;
import com.cmwebgame.entities.ModuleContent;
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


/**
 * @author wilson
 */
public class GenericDataAccessDriver extends DataAccessDriver {


	private static MemberManager membeManager = new MemberDao();

	private static MemberSessionManager memberSessionManager = new MemberSessionDao();
	
	private static AuthorityManager authorityManager = new AuthorityDao();
	
	private static CompanyManager companyManager = new CompanyDao();
	
	private static CostManager costManager = new CostDao();
	
	private static DepartmentManager departmentManager = new DepartmentDao();
	
	private static EmployeeManager employeeManager = new EmployeeDao();
	
	private static FunctionsManager functionsManager = new FunctionsDao();
	
	private static PositionManager positionManager = new PositionDao();
	
	private static ProjectEmployeeManager projectEmployeeManager = new ProjectEmployeeDao();
	
	private static ProjectManager projectManager = new ProjectDao();
	
	private static PerformanceManager performanceManager = new PerformanceDao();
	
	private static ModuleManager moduleManager = new ModuleDao();
	
	private static ModuleContentManager moduleContentManager = new ModuleContentDao();
	
	private static ApportionmentManager apportionmentManager = new ApportionmentDao();
	
	private static MessageManager messageManager = new MessageDao();
	
	private static SalaryManager salaryManager = new SalaryDao();

	@Override
	public MemberManager newMemberManager() {
		return membeManager;
	}

	@Override
	public MemberSessionManager newMemberSessionManager() {
		return memberSessionManager;
	}

	@Override
	public AuthorityManager newAuthorityManager() {
		// TODO Auto-generated method stub
		return authorityManager;
	}

	@Override
	public CompanyManager newCompanyManager() {
		// TODO Auto-generated method stub
		return companyManager;
	}

	@Override
	public CostManager newCostManager() {
		// TODO Auto-generated method stub
		return costManager;
	}

	@Override
	public DepartmentManager newDepartmentManager() {
		// TODO Auto-generated method stub
		return departmentManager;
	}

	@Override
	public EmployeeManager newEmployeeManager() {
		// TODO Auto-generated method stub
		return employeeManager;
	}

	@Override
	public FunctionsManager newFunctionsManager() {
		// TODO Auto-generated method stub
		return functionsManager;
	}

	@Override
	public PositionManager newPositionManager() {
		// TODO Auto-generated method stub
		return positionManager;
	}

	@Override
	public ProjectEmployeeManager newProjectEmployeeManager() {
		// TODO Auto-generated method stub
		return projectEmployeeManager;
	}

	@Override
	public ProjectManager newProjectManager() {
		// TODO Auto-generated method stub
		return projectManager;
	}

	@Override
	public PerformanceManager newPerformanceManager() {
		// TODO Auto-generated method stub
		return performanceManager;
	}

	@Override
	public ModuleManager newModuleManager() {
		// TODO Auto-generated method stub
		return moduleManager;
	}

	@Override
	public ModuleContentManager newModuleContentManager() {
		// TODO Auto-generated method stub
		return moduleContentManager;
	}

	@Override
	public ApportionmentManager newApportionmentManager() {
		// TODO Auto-generated method stub
		return apportionmentManager;
	}

	@Override
	public MessageManager newmMessageManager() {
		// TODO Auto-generated method stub
		return messageManager;
	}

	@Override
	public SalaryManager newSalaryManager() {
		// TODO Auto-generated method stub
		return salaryManager;
	}

}
