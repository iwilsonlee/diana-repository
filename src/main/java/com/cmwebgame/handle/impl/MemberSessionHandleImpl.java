package com.cmwebgame.handle.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.cmwebgame.GPortalExecutionContext;
import com.cmwebgame.context.RequestContext;
import com.cmwebgame.context.SessionContext;
import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.entities.Member;
import com.cmwebgame.entities.MemberSession;
import com.cmwebgame.exceptions.DatabaseException;
import com.cmwebgame.exceptions.PortalException;
import com.cmwebgame.handle.MemberSessionHandle;
import com.cmwebgame.repository.MemberSessionFacade;
import com.cmwebgame.service.MemberSessionManager;
import com.cmwebgame.sso.SSO;
import com.cmwebgame.sso.SSOUtils;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.SystemGlobals;
import com.cmwebgame.view.common.MemberProcess;

public class MemberSessionHandleImpl implements MemberSessionHandle {
	
	private static final Logger logger = Logger.getLogger(MemberSessionHandleImpl.class);

	
	/**
	 * Do a refresh in the user's session. This method will update the last visit time for the
	 * current user, as well checking for authentication if the session is new or the SSO user has
	 * changed
	 */
	@Override
	public void refreshSession() {
		//		Cookie cookieSession = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA));
		//		String sessionId = null;
		//		if(cookieSession != null){
		//			sessionId = cookieSession.getValue();
		//		}

		MemberSession memberSession = MemberSessionFacade.getMemberSession();

		RequestContext request = GPortalExecutionContext.getRequest();
		if (memberSession == null) {
			memberSession = this.restoreMemberSession();
			MemberSessionFacade.add(memberSession);
//			memberSession = new MemberSession();
//			memberSession.registerBasicInfo();
//			memberSession.setSessionId(request.getSessionContext().getId());
//			memberSession.setIp(request.getRemoteAddr());
//			MemberSessionFacade.makeUnlogged();
//
//			if (!GPortalExecutionContext.getForumContext().isBot()) {
//				// Non-SSO authentications can use auto login 如果不使用單點登入方案
//				if (!ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
//					if (SystemGlobals.getBoolValue(ConfigKeys.AUTO_LOGIN_ENABLED)) {
//						this.checkAutoLogin(memberSession);
//					} else {
//						memberSession.makeAnonymous();
//					}
//				} else {
//					this.checkSSO(memberSession);
//				}
//			}
//
//			MemberSessionFacade.add(memberSession);
		} else if (ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
			SSO sso;

			try {
				sso = (SSO) Class.forName(SystemGlobals.getValue(ConfigKeys.SSO_IMPLEMENTATION)).newInstance();
			} catch (Exception e) {
				throw new PortalException(e);
			}

			// If SSO, then check if the session is valid
			if (!sso.isSessionValidBySeesionId(memberSession, request)) {
				MemberSessionFacade.remove(memberSession.getSessionId());
				refreshSession();
			}
		} else {
			MemberSessionFacade.getMemberSession().updateSessionTime();
		}

	}
	
	public MemberSession restoreMemberSession(){
		RequestContext request = GPortalExecutionContext.getRequest();
		MemberSession memberSession = new MemberSession();
		memberSession.registerBasicInfo();
		memberSession.setSessionId(request.getSessionContext().getId());
		memberSession.setIp(request.getRemoteAddr());
		MemberSessionFacade.makeUnlogged();

		if (!GPortalExecutionContext.getForumContext().isBot()) {
			// Non-SSO authentications can use auto login 如果不使用單點登入方案
			if (!ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
				if (SystemGlobals.getBoolValue(ConfigKeys.AUTO_LOGIN_ENABLED)) {
					try {
						memberSession = this.checkAutoLogin(memberSession);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					memberSession.makeAnonymous();
				}
			} else {
				this.checkSSO(memberSession);
			}
		}

		
		return memberSession;
	}
	
	/**
	 * Checks user credentials / automatic login.
	 * 
	 * @param userSession The UserSession instance associated to the user's session
	 * @return <code>true</code> if auto login was enabled and the user was sucessfuly 
	 * logged in.
	 * @throws DatabaseException
	 */
	public MemberSession checkAutoLogin(Object ms) throws IOException, ClassNotFoundException {
		MemberSession memberSession = (MemberSession)ms;
		String tmpSessionId = GPortalExecutionContext.getRequest().getSessionContext().getId();
		RequestContext request = GPortalExecutionContext.getRequest();
		String result = MemberProcess.decodeMemberSession(request);
		if(result != null){
			String[] results = result.split("&");
			if(results.length != 2){
				if(memberSession == null){
					memberSession = new MemberSession();
					memberSession.makeAnonymous();
				}
				return memberSession;
			}
			String sessionId = results[0];
			String username = results[1];
			memberSession = MemberSessionFacade.getmeMemberSessionFromCache(sessionId);//getMemberSession(sessionId);
			if(memberSession == null ||memberSession.getUsername().equals(SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USERNAME))){
				if(username != null && !username.equals(SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USERNAME))){
					Member member = DataAccessDriver.getInstance().newMemberManager().getByUserName(username, Member.NORMAL);
					if(memberSession == null){
						memberSession = new MemberSession();
					}
					if(member == null){
						memberSession.makeAnonymous();
//						try {
//							if(memberSession != null){
//								//如果memseSesion是匿名的，則把cookie中的自動登入數據清除
//							    if(memberSession.getMemberId() == null || memberSession.getMemberId().intValue() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)){
//							    	Cookie cookie = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_MEMBERSESSION_DATA));
//							    	if(cookie != null && !StringUtils.isBlank(cookie.getValue())){
//							    		MemberProcess.clearCookieForSite();
//							    	}
//							    	
//							    }
//							}
//							
//							
//						} catch (Exception e) {
//							// TODO: handle exception
//							e.printStackTrace();
//						}
						return memberSession;
					}
					
					//					ResponseContext response = GPortalExecutionContext.getResponse();
//					MemberProcess.padMemberPropertyProcess(member, request);
					//					MemberProcess.loginSuccessProcess(memberSession, member, request, response);
					memberSession.setSessionId(tmpSessionId);
					this.configureMemberSession(memberSession, member);
					MemberSessionFacade.add(memberSession);
					return memberSession;
				}else {
					if(memberSession == null){
						memberSession = new MemberSession();
					}
					memberSession.makeAnonymous();
					
					return memberSession;
				}
				
				
			}else {
				memberSession.setSessionId(tmpSessionId);
				MemberSessionFacade.add(memberSession);
				return memberSession;
			}
		}else {
			if(memberSession == null){
				memberSession = new MemberSession();
			}
			memberSession.makeAnonymous();
			
			return memberSession;
		}
	
		
	}
	
	/**
	 * Setup optios and values for the user's session if authentication was ok.
	 * 
	 * @param userSession The UserSession instance of the user
	 * @param user The User instance of the authenticated user
	 */
	public void configureMemberSession(Object ms, Object m) {
		MemberSession memberSession = (MemberSession)ms;
		Member member = (Member)m;
		memberSession.dataToMember(member);

		// As an user may come back to the forum before its
		// last visit's session expires, we should check for
		// existent user information and then, if found, store
		// it to the database before getting his information back.
		String sessionId = MemberSessionFacade.isMemberInSession(member.getId().intValue());

		MemberSession tmpUs;
		if (sessionId != null) {
			MemberSessionFacade.storeSessionData(sessionId, GPortalExecutionContext.getConnection());
			tmpUs = MemberSessionFacade.getMemberSession(sessionId);
			MemberSessionFacade.remove(sessionId);
		} else {
			MemberSessionManager sm = DataAccessDriver.getInstance().newMemberSessionManager();
			tmpUs = sm.getById(memberSession, GPortalExecutionContext.getConnection());
		}

		if (tmpUs == null) {
			memberSession.setLastVisit(new Date(System.currentTimeMillis()));
		} else {
			// Update last visit and session start time
			memberSession.setLastVisit(new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
		}

		// If the execution point gets here, then the user
		// has chosen "autoLogin"
		memberSession.setAutoLogin(true);
		MemberSessionFacade.makeLogged();

		//		I18n.load(user.getLang());
	}
	


	/**
	 * Checks for user authentication using some SSO implementation
	 * @param userSession UserSession
	 */
	public void checkSSO(Object ms) {
		MemberSession memberSession = (MemberSession)ms;
		try {
			SSO sso = (SSO) Class.forName(SystemGlobals.getValue(ConfigKeys.SSO_IMPLEMENTATION)).newInstance();
			String username = sso.authenticateUser(GPortalExecutionContext.getRequest());

			if (username == null || username.trim().equals("")) {
				memberSession.makeAnonymous();
			} else {
				SSOUtils utils = new SSOUtils();

				//如果username的Member Object在資料庫中不存在,新增member
				if (!utils.memberExists(username)) {
					SessionContext session = GPortalExecutionContext.getRequest().getSessionContext();

					//					String email = (String) session.getAttribute(SystemGlobals.getValue(ConfigKeys.SSO_EMAIL_ATTRIBUTE));
					//取得cookie中的email
					String email = sso.authenticateEmail(GPortalExecutionContext.getRequest());
					//					String email = null;
					String password = (String) session.getAttribute(SystemGlobals
							.getValue(ConfigKeys.SSO_PASSWORD_ATTRIBUTE));

					if (email == null) {
						email = SystemGlobals.getValue(ConfigKeys.SSO_DEFAULT_EMAIL);
					}

					if (password == null) {
						password = SystemGlobals.getValue(ConfigKeys.SSO_DEFAULT_PASSWORD);
					}

					utils.register(password, email);

				} else {

					//如果在資料庫中存在，更新User
					Member member = utils.getMember();
					String email = member.getEmail();
					//							String password = (String) session.getAttribute(SystemGlobals.getValue(ConfigKeys.SSO_PASSWORD_ATTRIBUTE));
					String password = member.getPassword();
					utils.updater(username, password, email);
					//在單點登錄時，在此初始化會員等級資料
//					RequestContext request = GPortalExecutionContext.getRequest();
					//							ResponseContext response = GPortalExecutionContext.getResponse();
//					MemberProcess.padMemberPropertyProcess(member, request);
					//							MemberProcess.loginSuccessProcess(memberSession, member, request, response);
					this.configureMemberSession(memberSession, member);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortalException("Error while executing SSO actions: " + e);
		}
	}

	@Override
	public boolean isLogged() {
		// TODO Auto-generated method stub
		return MemberSessionFacade.isLogged();
	}

	@Override
	public void setMemberSessionToContext(ServletContext context) {
		// TODO Auto-generated method stub
		MemberSession ms =MemberSessionFacade.getMemberSession();
		if(ms != null){
			//如果memseSesion是匿名的，則把cookie中的自動登入數據清除
		    if(ms.getMemberId() == null || ms.getMemberId().intValue() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)){
		    	
		    	/*Cookie cookie = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_MEMBERSESSION_DATA));
		    	if(cookie != null && !StringUtils.isBlank(cookie.getValue())){
		    		MemberProcess.clearCookieForSite();
		    	}*/
		    	
		    }
		}
		context.setAttribute("session", ms);
	}

	@Override
	public void destroyedMemberSession(String sessionId) {
		// TODO Auto-generated method stub
		try {
			MemberSessionFacade.storeSessionDataFromCache(sessionId);
		}
		catch (Exception e) {
			logger.warn(e);
		}

		MemberSessionFacade.remove(sessionId);
	}

	@Override
	public BufferedImage getCaptchaImage() {
		// TODO Auto-generated method stub
		MemberSession memberSession = MemberSessionFacade.getMemberSession();
		BufferedImage image = memberSession.getCaptchaImage();
		return image;
	}

	@Override
	public boolean isModerator() {
		// TODO Auto-generated method stub
		return MemberSessionFacade.getMemberSession().isModerator();
	}
	
	/**
	 * @author wilson
	 */
	@Override
	public void writeCaptchaImage(Object memberSession) {
		BufferedImage image = ((MemberSession)memberSession).getCaptchaImage();
		
		if (image == null) {
			return;
		}
		
		OutputStream outputStream = null;
		
		try {
			outputStream = GPortalExecutionContext.getResponse().getOutputStream();
			ImageIO.write(image, "jpg", outputStream);
		} catch (IOException ex) {
			logger.error(ex);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}


}
