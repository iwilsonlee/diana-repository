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
 * Created on Jan 3, 2005 1:20:24 PM
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.sso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.cmwebgame.GPortalExecutionContext;
import com.cmwebgame.entities.Member;
import com.cmwebgame.exceptions.PortalException;
import com.cmwebgame.service.MemberManager;
import com.cmwebgame.util.DbUtils;
import com.cmwebgame.util.MD5;
import com.cmwebgame.util.preferences.SystemGlobals;


/**
 * Default login authenticator for CMWEBGAME.
 * This authenticator will validate the input against
 * <i>jforum_users</i>. 
 * 
 * @author Rafael Steil
 * @version $Id: DefaultLoginAuthenticator.java,v 1.10 2007/07/28 14:17:10 rafaelsteil Exp $
 */
public class DefaultLoginAuthenticator implements LoginAuthenticator
{
	private MemberManager memberModel;

	/**
	 * @see com.cmwebgame.sso.LoginAuthenticator#setUserModel(com.cmwebgame.dao.UserDAO)
	 */
	public void setMemberModel(Object memberModel)
	{
		this.memberModel = (MemberManager)memberModel;
	}

	/**
	 * @see com.cmwebgame.sso.LoginAuthenticator#validateLogin(String, String, java.util.Map) 
	 */
	public Member validateLogin(String username, String password, Map extraParams)
	{
		Member member = null;
		ResultSet rs=null;
		PreparedStatement p=null;
		
		try 
		{
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.login"));
			p.setString(1, username);
			p.setString(2, password);
			p.setInt(3, Member.NORMAL);

			rs = p.executeQuery();
			if (rs.next() && rs.getLong("id") > 0) {
				member = this.memberModel.getByIdAndStatus(rs.getLong("id"),Member.NORMAL);
			}
		}
		catch (SQLException e)
		{
			throw new PortalException(e);
		}
		finally
		{
			DbUtils.close(rs, p);
		}

		if (member != null) {
			return member;
		}

		return null;
	}
}
