package com.cmwebgame.sso;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.cmwebgame.ControllerUtils;
import com.cmwebgame.context.RequestContext;
import com.cmwebgame.entities.MemberSession;
import com.cmwebgame.util.EscapeUnescape;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.SystemGlobals;

public class CookieUserSSO implements SSO {

	static final Logger logger = Logger.getLogger(CookieUserSSO.class.getName());
	
	@Override
	public String authenticateSession(RequestContext request){
		Cookie cookieSessionId = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_DATA));
		String sessionId = null;
		if(cookieSessionId != null){
			sessionId = EscapeUnescape.unescape(cookieSessionId.getValue());
			if(sessionId != null && !sessionId.trim().equals("")){
				return sessionId;
			}
		}
		return null;
	}

	@Override
	public String authenticateUser(RequestContext request) {
		Cookie cookieNameUser = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_USER));
		String username = null;
		if (cookieNameUser != null) {
			username = EscapeUnescape.unescape(cookieNameUser.getValue());
			username = username.replace("#", "@");
			if (username.contains(":")) {
				username = null;
			}

			if (username != null && (username.trim().equals("null") || username.trim().equals(""))) {
				username = null;
			}

		}

		return username; // return username for jforum
		// jforum will use this name to regist database or set in HttpSession
	}

	@Override
	public String authenticateEmail(RequestContext request) {
		// login cookie set by my web LOGIN application
		Cookie cookieNameEmail = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_EMAIL));
		String email = null;

		if (cookieNameEmail != null) {
			email = cookieNameEmail.getValue();
		}
		return email; // return email for jforum
		// jforum will use this name to regist database or set in HttpSession
	}

	@Override
	public boolean isSessionValid(MemberSession memberSession, RequestContext request) {
		Cookie cookieNameUser = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_USER)); // user cookie
		String remoteUser = null;

		if (cookieNameUser != null) {
			remoteUser = cookieNameUser.getValue(); // gportal username
		}

		if (remoteUser == null
				&& memberSession.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			// user has since logged out
			return false;
		} else if (remoteUser != null
				&& memberSession.getMemberId().intValue() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			// anonymous user has logged in
			return false;
		} else if (remoteUser != null && !remoteUser.equals(memberSession.getUsername())) {
			// not the same user (cookie and session)
			return false;
		}
		return true; // myapp user and forum user the same. valid user.
	}
	
	@Override
	public boolean isSessionValidBySeesionId(MemberSession memberSession, RequestContext request) {
		Cookie cookieSession = ControllerUtils.getCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA)); // user cookie
		String remoteSession = null;
		
		if (cookieSession != null) {
			remoteSession = cookieSession.getValue(); // gportal username
		}
		
		if (remoteSession == null
				&& memberSession.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			// user has since logged out
			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA), "");
			return false;
		} else if (remoteSession != null
				&& memberSession.getMemberId().intValue() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			// anonymous user has logged in
			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA), "");
			return false;
		} else if (remoteSession != null && !remoteSession.equals(memberSession.getSessionId())) {
			// not the same user (cookie and session)
			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA), "");
			return false;
		}
		return true; // myapp user and forum user the same. valid user.
	}

}
