/*
 * Copyright (c) CMWEBGAME Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * This file creation date: 12/03/2004 - 18:47:26
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.repository;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.cmwebgame.ControllerUtils;
import com.cmwebgame.DBConnection;
import com.cmwebgame.GPortalExecutionContext;
import com.cmwebgame.cache.CacheEngine;
import com.cmwebgame.cache.Cacheable;
import com.cmwebgame.cache.CacheableTwoLevel;
import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.entities.MemberSession;
import com.cmwebgame.handle.MemberSessionHandle;
import com.cmwebgame.util.ReflectionUtils;
import com.cmwebgame.util.VipUtil;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.ImplGlobals;
import com.cmwebgame.util.preferences.SystemGlobals;
import com.cmwebgame.view.common.MemberProcess;
import com.google.common.collect.Lists;
import com.octo.captcha.image.gimpy.Gimpy;

/**
 * 实现二级缓存接口CacheableTwoLevel，如只要使用一级缓存，则须实现一级缓存接口Cacheable
 * @author wilson
 */
public class MemberSessionFacade implements Cacheable {
	
	private static final Logger logger = Logger.getLogger(MemberSessionFacade.class);

	private static MemberSessionHandle  memberSessionHandle = (MemberSessionHandle) ImplGlobals.getHandle("MemberSessionHandleImpl");
	
	private static final String FQN = "sessions";
	private static final String FQN_LOGGED = FQN + "_logged";
	private static final String FQN_COUNT = FQN + "_count";
	private static final String FQN_MEMBER_SERVER = FQN + "_member_server";
	private static final String FQN_USER_ID = FQN + "_memberId";
	private static final String ANONYMOUS_COUNT = "anonymousCount";
	private static final String LOGGED_COUNT = "loggedCount";

	private static CacheEngine cache;

	/**
	 * @see com.cmwebgame.cache.CacheableTwoLevel#setCacheEngine(com.cmwebgame.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine) {
		cache = engine;
	}


	/**
	 * Add a new <code>UserSession</code> entry to the session.
	 * This method will make a call to <code>CMWEBGAME.getRequest.getSession().getId()</code>
	 * to retrieve the session's id
	 * 
	 * @param us The user session objetc to add
	 * @see #add(UserSession, String)
	 */
	public static void add(MemberSession ms) {
//		addMemberSession(ms, GPortalExecutionContext.getRequest().getSessionContext().getId());
		add(ms, GPortalExecutionContext.getRequest().getSessionContext().getId());
	}

	public static void update(MemberSession ms) {
		update(ms, GPortalExecutionContext.getRequest().getSessionContext().getId());
	}

	/**
	 * Registers a new {@link UserSession}.
	 * <p>
	 * If a call to {@link UserSession#getUserId()} return a value different 
	 * of <code>SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)</code>, then 
	 * the user will be registered as "logged". Otherwise it will enter as anonymous.
	 * </p>
	 * 
	 * <p>
	 * Please note that, in order to keep the number of guest and logged users correct, 
	 * it's caller's responsability to {@link #remove(String)} the record before adding it
	 * again if the current session is currently represented as "guest". 
	 * </p>
	 *  
	 * @param us the UserSession to add
	 * @param sessionId the user's session id
	 */
	public static void add(MemberSession ms, String sessionId) {
		Cookie cookieSession = null;
		try {
			cookieSession = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		String cookieSessionId = null;
		if (cookieSession != null) {
			cookieSessionId = cookieSession.getValue();
			if (cookieSessionId != null && !cookieSessionId.trim().equals("") && !cookieSessionId.trim().equals("null")) {
				//				if(getMemberSession(cookieSessionId) != null){
				//					update(ms, cookieSessionId);
				//				}else{
				//					addMemberSession(ms, cookieSessionId);
				//				}
				addMemberSession(ms, cookieSessionId);
//				GPortalExecutionContext.getRequest().getSessionContext().setAttribute("cookieSessionId",
//						cookieSessionId);
			} else {
				addMemberSession(ms, sessionId);
			}
		} else {
			addMemberSession(ms, sessionId);
		}

		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA), ms.getSessionId());
	}

	public static void addMemberSession(MemberSession ms, String sessionId) {
		if (ms.getSessionId() == null || ms.getSessionId().equals("") || !ms.getSessionId().equals(sessionId)) {
			ms.setSessionId(sessionId);
		}

		//是否為VIP
		ms.setVip(VipUtil.isVip(ms.getMemberId()));

		synchronized (FQN) {
			cache.add(FQN, ms.getSessionId(), ms);
			
			if (!GPortalExecutionContext.getForumContext().isBot()) {
				if (ms.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					changeUserCount(LOGGED_COUNT, true);
					cache.add(FQN_LOGGED, ms.getSessionId(), ms);
					cache.add(FQN_USER_ID, Integer.toString(ms.getMemberId().intValue()), ms.getSessionId());
				} else {
					// TODO: check the anonymous IP constraint
					changeUserCount(ANONYMOUS_COUNT, true);
				}
			}
		}
		MemberProcess.saveSessionInfoToCooke(ms, GPortalExecutionContext.getRequest());
	}
	
	

	public static void update(MemberSession ms, String sessionId) {
		if (ms.getSessionId() == null || ms.getSessionId().equals("")) {
			ms.setSessionId(sessionId);
		}

		//是否為VIP
		ms.setVip(VipUtil.isVip(ms.getMemberId()));

		synchronized (FQN) {
			cache.add(FQN, ms.getSessionId(), ms);
			if (!GPortalExecutionContext.getForumContext().isBot()) {
				if (ms.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					cache.add(FQN_LOGGED, ms.getSessionId(), ms);
					cache.add(FQN_USER_ID, Integer.toString(ms.getMemberId().intValue()), ms.getSessionId());
				}
			}
		}

	}

	private static void changeUserCount(String cacheEntryName, boolean increment) {
		Integer count = new Integer(0);
//		Object object = cache.get(FQN_COUNT, cacheEntryName);
		Object object = null;
		if (object != null) {
			count = (Integer) object;
		} else {
			object = cache.get(FQN_COUNT, cacheEntryName);
			count = (Integer) object;
		}

		if (count == null) {
			count = new Integer(0);
		}

		if (increment) {
			count = new Integer(count.intValue() + 1);
		} else if (count.intValue() > 0) {
			count = new Integer(count.intValue() - 1);
		}

		cache.add(FQN_COUNT, cacheEntryName, count);
	}

	/**
	 * Add a new entry to the user's session
	 * 
	 * @param name The attribute name
	 * @param value The attribute value
	 */
	public static void setAttribute(String name, Object value) {
		GPortalExecutionContext.getRequest().getSessionContext().setAttribute(name, value);
	}

	/**
	 * Removes an attribute from the session
	 * 
	 * @param name The key associated to the the attribute to remove
	 */
	public static void removeAttribute(String name) {
		GPortalExecutionContext.getRequest().getSessionContext().removeAttribute(name);
	}

	/**
	 * Gets an attribute value given its name
	 * 
	 * @param name The attribute name to retrieve the value
	 * @return The value as an Object, or null if no entry was found
	 */
	public static Object getAttribute(String name) {
		return GPortalExecutionContext.getRequest().getSessionContext().getAttribute(name);
	}

	/**
	 * Remove an entry fro the session map
	 * 
	 * @param sessionId The session id to remove
	 */
	public static void remove(String sessionId) {
//		if (cache == null) {
//			logger.warn("Got a null cache instance. #" + sessionId);
//			return;
//		}

		synchronized (FQN) {
//			MemberSession ms = getmeMemberSession(sessionId);
			MemberSession ms = getmeMemberSessionFromCache(sessionId);

			if (ms != null) {
				cache.remove(FQN_LOGGED, sessionId);
				cache.remove(FQN_USER_ID, Integer.toString(ms.getMemberId().intValue()));
				cache.remove(FQN_MEMBER_SERVER, Integer.toString(ms.getMemberId().intValue()));

				if (ms.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					changeUserCount(LOGGED_COUNT, false);
				} else {
					changeUserCount(ANONYMOUS_COUNT, false);
				}
			}

			if(cache != null){
				cache.remove(FQN, sessionId);
				if(logger.isDebugEnabled()){
					logger.debug("Removing session " + sessionId);
				}
			}else {
				if(logger.isDebugEnabled()){
					logger.debug("Removing session " + sessionId + " | the memcache is null !");
				}
			}
			
			try {
//				ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA), "");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * Get all registered sessions
	 * 
	 * @return <code>ArrayList</code> with the sessions. Each entry
	 * is an <code>UserSession</code> object.
	 */
	public static List getAllSessions() {
		synchronized (FQN) {
			List<MemberSession> msList = new ArrayList<MemberSession>();
//			Object object = cache.getValues(FQN);
			Object object = null;
			if (object != null) {
				msList = (List<MemberSession>) object;
				return new ArrayList(msList);
			} else {
				Collection list = new ArrayList();
				object = cache.getValues(FQN);
				if(object != null){
					list = (Collection) object;
				}
				msList = new ArrayList(list);
//				for (MemberSession ms : msList) {
//					cache.add(FQN, ms.getSessionId(), ms);
//				}
				return msList;
			}

		}
	}

	/**
	 * 獲取指定會員的所有注冊游戲伺服器id
	 * @return
	 */
	public static List<Long> findServerIdsByMemberId(Long memberId) {
		synchronized (FQN_MEMBER_SERVER) {
			List<Long> msList = new ArrayList<Long>();
//			Object object = cache.get(FQN_MEMBER_SERVER, memberId.toString());
			Object object = null;
			if (object != null) {
				msList = (List<Long>) object;
			} else {
				object = cache.get(FQN_MEMBER_SERVER, memberId.toString());
				if (object != null) {
					msList = (List<Long>) object;
//					cache.add(FQN_MEMBER_SERVER, memberId.toString(), msList);
				} else {
					msList = DataAccessDriver.getInstance().newMemberManager().findServerIdsByMemberId(memberId);
					cache.add(FQN_MEMBER_SERVER, memberId.toString(), msList);
				}
			}

			return new ArrayList(msList);
		}
	}

	/**
	 * 更新缓存内指定會員的注冊游戲伺服器列表
	 * @param memberId
	 * @return
	 */
	public static List<Long> updateServerIdsByMemberId(Long memberId) {
		synchronized (FQN_MEMBER_SERVER) {
			List<Long> msList = new ArrayList<Long>();
			msList = DataAccessDriver.getInstance().newMemberManager().findServerIdsByMemberId(memberId);
			cache.add(FQN_MEMBER_SERVER, memberId.toString(), msList);

			return new ArrayList(msList);
		}
	}

	/**
	 * 判斷指定會員是否已經在指定的游戲伺服器注冊
	 * 已注冊則返回true，否則返回false
	 * @param memberId
	 * @param serverId
	 * @return
	 */
	public static boolean checkExistByMemberIdAndServerId(Long memberId, Long serverId) {
		boolean exist = false;
		List<Long> serverIds = findServerIdsByMemberId(memberId);
		for (Long id : serverIds) {
			if (id.equals(serverId)) {
				exist = true;
				break;
			}
		}
		return exist;
	}

	/**
	 * Gets the {@link UserSession} instance of all logged users
	 * @return A list with the user sessions
	 */
	public static List getLoggedSessions() {
		synchronized (FQN) {
			List<MemberSession> msList = new ArrayList<MemberSession>();
			Object object = cache.getValues(FQN_LOGGED);
			if (object != null) {
				msList = (List<MemberSession>) object;
				return new ArrayList(msList);
			} else {
				
				return Lists.newArrayList();
			}
		}
	}

	/**
	 * Get the number of logged users
	 * @return the number of logged users
	 */
	public static int registeredSize() {
		Integer count = new Integer(0);
		Object object = cache.get(FQN_COUNT, LOGGED_COUNT);
		if (object != null) {
			count = (Integer) object;
		}

		return (count == null ? 0 : count.intValue());
	}

	/**
	 * Get the number of anonymous users
	 * @return the nuber of anonymous users
	 */
	public static int anonymousSize() {
		Integer count = new Integer(0);
		Object object = cache.get(FQN_COUNT, ANONYMOUS_COUNT);
		if (object != null) {
			count = (Integer) object;
		}

		return (count == null ? 0 : count.intValue());
	}

	public static void clear() {
		synchronized (FQN) {
			MemberSession memberSession = MemberSessionFacade.getMemberSession();

			cache.remove(FQN);
			cache.add(FQN, memberSession.getSessionId(), memberSession);
			cache.add(FQN_COUNT, LOGGED_COUNT, new Integer(0));
			cache.add(FQN_COUNT, ANONYMOUS_COUNT, new Integer(0));
			cache.remove(FQN_LOGGED);
			cache.remove(FQN_USER_ID);
		}
	}

	/**
	 * Gets the user's <code>UserSession</code> object
	 * 
	 * @return The <code>UserSession</code> associated to the user's session
	 */
	public static MemberSession getMemberSession() {
//		return getMemberSessionDo2(GPortalExecutionContext.getRequest().getSessionContext().getId());
	//	GPortalExecutionContext.getRequest().getSessionContext().invalidate();
		return getMemberSession(GPortalExecutionContext.getRequest().getSessionContext().getId());
	}
/**
	public static MemberSession getMemberSession(String sessionId)
	{
		MemberSession ms = null;
		if (cache != null) {
			Object object = cache.get(FQN, sessionId);
			if (object != null) {
				ms = putSessionData(object);
				if (ms.getMemberId().intValue() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					object = memCache.get(FQN, sessionId);
					if (object != null) {
						ms = putSessionData(object);
						//								if(ms != null){
						//									add(ms, sessionId);
						//								}
					}
				}
			} else {
				object = memCache.get(FQN, sessionId);
				if (object != null) {
					ms = putSessionData(object);
					//							if(ms != null){
					//								add(ms, sessionId);
					//							}
				}
			}
			return (ms != null ? ms : null);
		}

		logger.warn("Got a null cache in getUserSession. #" + sessionId);
		return null;
	}
	*/
	
	
	/**
	 * Gets an {@link MemberSession} by the session id.
	 * 
	 * @param sessionId the session's id
	 * @return an <b>immutable</b> MemberSession, or <code>null</code> if no entry found
	 */
//	public static MemberSession getMemberSession(String sessionId) {
//		MemberSession ms = null;
//		try {
//			ms = MemberProcess.restoreMemberSession(GPortalExecutionContext.getRequest());
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(ms == null){
//			remove(sessionId);
//		}
//		return ms;
//	}
	
	/**
	 * 從cache里獲取MemberSession資料
	 * @param sessionId
	 * @return
	 */
	public static MemberSession getmeMemberSessionFromCache(String sessionId){
		Object oj = null;
		MemberSession ms = null;
		if (cache != null) {
			oj = cache.get(FQN, sessionId);
			if(oj != null){
				ms = putSessionData(oj);
				if(ms != null){
					return ms;
				}
			}
			
		}
		return null;
	}
	
	public static MemberSession getMemberSession(String sessionId) {
		
		String result = null;
		try {
			result = MemberProcess.decodeMemberSession(GPortalExecutionContext.getRequest());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(result != null){
			String[] results = result.split("&");
			if(results != null && results.length == 2){
				sessionId = results[0];
//				String username = results[1];
			}
		}
		
		
		MemberSession ms = null;
		
		ms = getmeMemberSessionFromCache(sessionId);
		
		
		if (ms != null) {

			if(ms.getUsername().trim().equals(SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USERNAME))){
				try {
//					ControllerUtils controllerUtils = new ControllerUtils();
					MemberSession tmpms = (MemberSession)memberSessionHandle.restoreMemberSession();//controllerUtils.restoreMemberSession();
//					MemberSession tmpms = MemberProcess.restoreMemberSessionFromCookie(GPortalExecutionContext.getRequest());
					if(!tmpms.getUsername().trim().equals(SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USERNAME))){
						ms = tmpms;
					}
					ms.setSessionId(sessionId);
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					ms = null ;
				}
				
			} else {
				try {
					String buType = ControllerUtils.getCookie(MemberProcess.COOKIE_BU_TYPE).getValue();
					logger.debug("Cookie " + MemberProcess.COOKIE_BU_TYPE + "=" + buType);
					ms.setBuType(Integer.parseInt(buType));
				} catch (Exception e) {
				}
			}
			
			//logger.debug("ms properties:{siteUid:"+ms.getSiteUid() +", sessionId:"+ms.getSessionId() + 
			//		 ", memberId "+ms.getMemberId() );
		
			return ms;
		} else {
			try {
//				ControllerUtils controllerUtils = new ControllerUtils();
				ms = (MemberSession)memberSessionHandle.restoreMemberSession();//controllerUtils.restoreMemberSession();
				ms.setSessionId(sessionId);
			} catch (Exception e) {
				e.printStackTrace();
				ms = null ;
			}
			
			return ms;
		}

	}

	

	/**
	 * Gets the number of session elements.
	 * 
	 * @return The number of session elements currently online (without bots)
	 */
	public static int size() {
		return (anonymousSize() + registeredSize());
	}

	/**
	 * Verify if the user in already loaded
	 * 
	 * @param username The username to check
	 * @return The session id if the user is already registered into the session, 
	 * or <code>null</code> if it is not.
	 */
	public static String isMemberSession(String username) {
		int aid = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);

		synchronized (FQN) {
			List<MemberSession> memberSessionList = new ArrayList<MemberSession>();
//			Object object = cache.getValues(FQN);
			Object object = null;
			if (object != null) {
				memberSessionList = (List<MemberSession>) object;
			}
			if (object == null || memberSessionList.size() == 0) {
				object = cache.getValues(FQN);
				memberSessionList = (List<MemberSession>) object;
				for (MemberSession ms : memberSessionList) {
					cache.add(FQN, ms.getSessionId(), ms);
				}
			}
			for (Iterator iter = memberSessionList.iterator(); iter.hasNext();) {
				MemberSession ms = (MemberSession) iter.next();
				String thisUsername = ms.getUsername();

				if (thisUsername == null) {
					continue;
				}

				if (ms.getMemberId().intValue() != aid && thisUsername.equals(username)) {
					return ms.getSessionId();
				}
			}
		}

		return null;
	}

	/**
	 * Verify if there is an user in the session with the 
	 * user id passed as parameter.
	 * 
	 * @param userId The user id to check for existance in the session
	 * @return The session id if the user is already registered into the session, 
	 * or <code>null</code> if it is not.
	 */
	public static String isMemberInSession(int userId) {
		String memberSessionId = "";
//		Object object = cache.get(FQN_USER_ID, Integer.toString(userId));
		Object object = null;
		if (object != null) {
			memberSessionId = (String) cache.get(FQN_USER_ID, Integer.toString(userId));
		} else {
			object = cache.get(FQN_USER_ID, Integer.toString(userId));
			memberSessionId = (String) cache.get(FQN_USER_ID, Integer.toString(userId));
//			cache.add(FQN_USER_ID, Integer.toString(userId), memberSessionId);
		}
		return memberSessionId;
	}

	/**
	 * Verify is the user is logged in.
	 * 
	 * @return <code>true</code> if the user is logged, or <code>false</code> if is 
	 * an anonymous user.
	 */
	public static boolean isLogged() {
		return "1".equals(MemberSessionFacade.getAttribute(ConfigKeys.LOGGED));
//		return "1".equals(ControllerUtils.getCookie(ConfigKeys.LOGGED));
	}

	/**
	 * Marks the current user session as "logged" in 
	 */
	public static void makeLogged() {
		MemberSessionFacade.setAttribute(ConfigKeys.LOGGED, "1");
//		ControllerUtils.addCookie(ConfigKeys.LOGGED, "1");
	}

	/**
	 * Marks the current user session as "logged" out
	 *
	 */
	public static void makeUnlogged() {
		MemberSessionFacade.removeAttribute(ConfigKeys.LOGGED);
//		ControllerUtils.addCookie(ConfigKeys.LOGGED, null);
	}



	/**
	 * Persists user session information.
	 * This method will get a <code>Connection</code> making a call to
	 * <code>DBConnection.getImplementation().getConnection()</code>, and
	 * then releasing the connection after the method is processed.   
	 * 
	 * @param sessionId The session which we're going to persist information
	 * @see #storeSessionData(String, Connection)
	 */
	public static void storeSessionData(String sessionId) {
		Connection conn = null;
		try {
			conn = DBConnection.getImplementation().getConnection();
			MemberSessionFacade.storeSessionData(sessionId, conn);
		} finally {
			if (conn != null) {
				try {
					DBConnection.getImplementation().releaseConnection(conn);
				} catch (Exception e) {
					logger.warn("Error while releasing a connection: " + e);
				}
			}
		}
	}
	public static void storeSessionDataFromCache(String sessionId) {
		Connection conn = null;
		try {
			conn = DBConnection.getImplementation().getConnection();
			MemberSessionFacade.storeSessionDataFromCache(sessionId, conn);
		} finally {
			if (conn != null) {
				try {
					DBConnection.getImplementation().releaseConnection(conn);
				} catch (Exception e) {
					logger.warn("Error while releasing a connection: " + e);
				}
			}
		}
	}

	/**
	 * Persists member session information.
	 * 
	 * @param sessionId The session which we're going to persist
	 * @param conn A <code>Connection</code> to be used to connect to
	 * the database. 
	 * @see #storeSessionData(String)
	 */
	public static void storeSessionData(String sessionId, Connection conn) {
		MemberSession ms = MemberSessionFacade.getMemberSession(sessionId);
		if (ms != null) {
			try {
				if (ms.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					DataAccessDriver.getInstance().newMemberSessionManager().update(ms, conn);
				}

			} catch (Exception e) {
				logger.warn("Error storing member session data: " + e, e);
			}
		}
	}
	public static void storeSessionDataFromCache(String sessionId, Connection conn) {
		MemberSession ms = MemberSessionFacade.getmeMemberSessionFromCache(sessionId);
		if (ms != null) {
			try {
				if (ms.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					DataAccessDriver.getInstance().newMemberSessionManager().update(ms, conn);
				}
				
			} catch (Exception e) {
				logger.warn("Error storing member session data: " + e, e);
			}
		}
	}

	public static MemberSession putSessionData(Object object) {
		MemberSession ms = new MemberSession();
		//		ms.setAmount(object.)
		boolean test = ReflectionUtils.isInstance(object, MemberSession.class);
		if (!test) {
			try {
				//				Object o = getPrivateProperty(object, "sessionId");
				ms.setAmount((Integer) ReflectionUtils.getPrivateProperty(object, "amount"));
				ms.setAutoLogin((Boolean) ReflectionUtils.getPrivateProperty(object, "autoLogin"));
				ms.setEmail((String) ReflectionUtils.getPrivateProperty(object, "email"));
				ms.setExperience((Integer) ReflectionUtils.getPrivateProperty(object, "experience"));
				ms.setIp((String) ReflectionUtils.getPrivateProperty(object, "ip"));
				ms.setLastVisit((Date) ReflectionUtils.getPrivateProperty(object, "lastVisit"));
				ms.setMemberId((Long) ReflectionUtils.getPrivateProperty(object, "memberId"));
				ms.setNickname((String) ReflectionUtils.getPrivateProperty(object, "nickname"));
				ms.setPassword((String) ReflectionUtils.getPrivateProperty(object, "password"));
				ms.setRestAmount((Integer) ReflectionUtils.getPrivateProperty(object, "restAmount"));
				ms.setScore((Integer) ReflectionUtils.getPrivateProperty(object, "score"));
				ms.setSessionId((String) ReflectionUtils.getPrivateProperty(object, "sessionId"));
				ms.setSessionTime((Long) ReflectionUtils.getPrivateProperty(object, "sessionTime"));
				ms.setStartTime((Date) ReflectionUtils.getPrivateProperty(object, "startTime"));
				ms.setUsername((String) ReflectionUtils.getPrivateProperty(object, "username"));
				ms.setAvatar((String) ReflectionUtils.getPrivateProperty(object, "avatar"));
				ms.setAvatarTmp((String) ReflectionUtils.getPrivateProperty(object, "avatarTmp"));
				ms.setAvatarStatus((Integer) ReflectionUtils.getPrivateProperty(object, "avatarStatus"));
				ms.setImageCaptcha((Gimpy) ReflectionUtils.getPrivateProperty(object, "imageCaptcha"));
				ms.setSiteUid((String)ReflectionUtils.getPrivateProperty(object, "siteUid"));
				ms.setVip((Boolean) ReflectionUtils.getPrivateProperty(object, "vip"));
				ms.setBuType((Integer) ReflectionUtils.getPrivateProperty(object, "buType"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			ms = (MemberSession) object;
		}

		return ms;
	}

}
