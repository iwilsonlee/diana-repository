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
 * Created on Mar 28, 2005 7:36:00 PM
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.sso;

import javax.servlet.http.Cookie;

import com.cmwebgame.ControllerUtils;
import com.cmwebgame.context.RequestContext;
import com.cmwebgame.entities.MemberSession;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.SystemGlobals;


/**
 * Simple SSO authenticator. This class will try to validate an user by simple checking
 * <code>request.getRemoteUser()</code> is not null.
 * 
 * @author Rafael Steil
 * @author Daniel Campagnoli
 * @version $Id: RemoteUserSSO.java,v 1.10 2006/08/23 02:13:53 rafaelsteil Exp $
 */
public class RemoteUserSSO implements SSO
{
	public String authenticateSession(RequestContext request){
		return request.getSessionContext().getId();
	}
	/**
	 * @see com.cmwebgame.sso.SSO#authenticateUser(com.cmwebgame.context.RequestContext)
     * @param request AWebContextRequest     * @return String
	 */
	public String authenticateUser(RequestContext request)
	{
		return request.getRemoteUser();
	}
	
	public String authenticateEmail(RequestContext request)
	{
		return request.getRemoteEmail();
	}

	public boolean isSessionValid(MemberSession memberSession, RequestContext request)
	{
		String remoteUser = request.getRemoteUser();

		// user has since logged out
		if (remoteUser == null && memberSession.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			return false;
		}
		// user has since logged in
		else if (remoteUser != null
				&& memberSession.getMemberId() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			return false;
		}
		// user has changed user
		else if (remoteUser != null && !remoteUser.equals(memberSession.getUsername())) {
			return false;
		}
        
		return true;
	}
	
	public boolean isSessionValidBySeesionId(MemberSession memberSession, RequestContext request) {
		String remoteSession = request.getSessionContext().getId();
		
		
		if (remoteSession == null
				&& memberSession.getMemberId().intValue() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			// user has since logged out
			return false;
		} else if (remoteSession != null
				&& memberSession.getMemberId().intValue() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			// anonymous user has logged in
			return false;
		} else if (remoteSession != null && !remoteSession.equals(memberSession.getSessionId())) {
			// not the same user (cookie and session)
			return false;
		}
		return true; // myapp user and forum user the same. valid user.
	}
}
